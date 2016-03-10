package com.ziben365.ocapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.ziben365.ocapp.DemoApplication;
import com.ziben365.ocapp.MainActivity;
import com.ziben365.ocapp.R;
import com.ziben365.ocapp.adapter.ChannelPersonalAdapter;
import com.ziben365.ocapp.constant.Link;
import com.ziben365.ocapp.constant.NotifyAction;
import com.ziben365.ocapp.constant.RequestCode;
import com.ziben365.ocapp.constant.RequestTag;
import com.ziben365.ocapp.inter.util.ProjectItemClickUtil;
import com.ziben365.ocapp.model.UserLike;
import com.ziben365.ocapp.model.UserMessage;
import com.ziben365.ocapp.model.UserRecommend;
import com.ziben365.ocapp.ui.AddProjectActivity;
import com.ziben365.ocapp.util.ErrorDealUtil;
import com.ziben365.ocapp.util.GsonUtil;
import com.ziben365.ocapp.util.L;
import com.ziben365.ocapp.util.SPUtils;
import com.ziben365.ocapp.util.request.VolleyInterface;
import com.ziben365.ocapp.util.request.VolleyRequest;
import com.ziben365.ocapp.view.AppProgressDialog;
import com.ziben365.ocapp.widget.refresh.ProgressStyle;
import com.ziben365.ocapp.widget.refresh.recycler.RefreshRecyclerView;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/14.
 * email  1956766863@qq.com
 */
public class ChannelPersonalFragment extends Fragment implements RefreshRecyclerView.LoadingListener {

    @InjectView(R.id.id_refresh_recyclerView)
    RefreshRecyclerView mRefreshRecyclerView;
    @InjectView(R.id.id_hint)
    TextView mHint;
    @InjectView(R.id.id_go_to)
    TextView mGoTo;
    @InjectView(R.id.id_empty_1)
    LinearLayout mEmpty1;
    @InjectView(R.id.id_empty)
    FrameLayout mEmpty;
    @InjectView(R.id.id_empty_image)
    ImageView mEmptyImage;
    /*@InjectView(R.id.id_refreshLayout)
    SwipeRefreshLayout mRefreshLayout;*/


    private ArrayList<UserRecommend> userRecommends;
    private ArrayList<UserLike> userLikes;
    private ArrayList<UserMessage> userMessages;
    private ArrayList<MyGold> userGolds;
    private ChannelPersonalAdapter adapter;
    private int channel = PersonalFragment.CHANNEL_RECOMMEND;
    private String url = Link.PROJECT_MY_RECOMMEND;
    private String tag = RequestTag.REQUEST_TAG_MY_RECOMMEND;
    private AppProgressDialog progressDialog;

    private LikeBroadcastReceiver broadcastReceiver;
    private RecommendBroadcastReceiver recBroadcastReceiver;
    private GoldBroadcastReceiver goldBroadcastReceiver;
    private UserMessageBroadcastReceiver userMessageBroadcastReceiver;

    private SharedPreferences sp;

    public static boolean isUpdateLike = false;

    private int page = 1;

    public static ChannelPersonalFragment newInstance(int channel) {
        ChannelPersonalFragment instance = new ChannelPersonalFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("channel", channel);
        instance.setArguments(bundle);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_personal, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        sp = getActivity().getSharedPreferences(PersonalFragment.FILE_DATA_NAME, Context.MODE_PRIVATE);

        channel = getArguments().getInt("channel");
        progressDialog = new AppProgressDialog(getActivity());
        userRecommends = new ArrayList<>();
        userLikes = new ArrayList<>();
        userMessages = new ArrayList<>();
        userGolds = new ArrayList<>();


        IntentFilter filter1 = new IntentFilter(NotifyAction.ACTION_UPDATE_USER_INFO);
        getActivity().registerReceiver(updateUserInfoBroadcastReceiver, filter1);

        switch (channel) {
            case PersonalFragment.CHANNEL_RECOMMEND:
                IntentFilter rec_filter = new IntentFilter(NotifyAction.ACTION_UPDATE_USER_RECOMMEND);
                recBroadcastReceiver = new RecommendBroadcastReceiver();
                getActivity().registerReceiver(recBroadcastReceiver, rec_filter);
                initRecommendData();

                break;
            case PersonalFragment.CHANNEL_LIKE:
                isUpdateLike = true;
                IntentFilter filter = new IntentFilter(NotifyAction.ACTION_UPDATE_USER_LIKE);
                broadcastReceiver = new LikeBroadcastReceiver();
                getActivity().registerReceiver(broadcastReceiver, filter);

                initLikeData();

                break;
            case PersonalFragment.CHANNEL_DOLLAR:
                goldBroadcastReceiver = new GoldBroadcastReceiver();
                IntentFilter filter2 = new IntentFilter(NotifyAction.ACTION_UPDATE_USER_GOLD);
                getActivity().registerReceiver(goldBroadcastReceiver, filter2);

                initGoldData();

                break;
            case PersonalFragment.CHANNEL_MESSAGE:
                userMessageBroadcastReceiver = new UserMessageBroadcastReceiver();
                IntentFilter intentFilter3 = new IntentFilter(NotifyAction.ACTION_USER_MESSAGE_CHANGE);
                getActivity().registerReceiver(userMessageBroadcastReceiver, intentFilter3);
                initMessageData();

                break;
        }


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //设置垂直排列
        mRefreshRecyclerView.setLayoutManager(layoutManager);
        //加载每条的动画
        mRefreshRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置刷新
        mRefreshRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRefreshRecyclerView.setLoadingMoreEnabled(true);
        mRefreshRecyclerView.setLoadingListener(this);
        mRefreshRecyclerView.setPullRefreshEnabled(false);

       /* mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                applyData(channel);
            }
        });*/

    }

    /**
     * 初始化我的收藏数据
     */
    private void initLikeData() {
        url = Link.PROJECT_MY_FAVORITES;
        tag = RequestTag.REQUEST_TAG_MY_FAVORITES;
        adapter = new ChannelPersonalAdapter(getActivity(), userLikes, channel);
        mRefreshRecyclerView.setAdapter(adapter);

        String resource1 = sp.getString(PersonalFragment.KEY_LIKE, "");
        if (TextUtils.isEmpty(resource1)) {
            applyData(0);
        } else {
            applyData(1);
            ArrayList<UserLike> likes = GsonUtil.getInstance().fromJson(
                    resource1, new TypeToken<ArrayList<UserLike>>() {
                    }.getType());
            userLikes.clear();
            userLikes.addAll(likes);
            adapter.notifyDataSetChanged();
            if (userLikes.size() == 0) {
                dealEmpty();
            } else {
                mEmpty.setVisibility(View.GONE);
                mEmpty1.setVisibility(View.GONE);
                mEmptyImage.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 初始化我的金币
     */
    private void initGoldData() {
        String total_gold = sp.getString(PersonalFragment.KEY_TOTAL_GOLD, "");
        String day_gold = sp.getString(PersonalFragment.KEY_DAY_GOLD, "");
        String rank = sp.getString(PersonalFragment.KEY_RANK, "");

        adapter = new ChannelPersonalAdapter(getActivity(), userGolds, channel);
        mRefreshRecyclerView.setAdapter(adapter);

        if (TextUtils.isEmpty(total_gold) | TextUtils.isEmpty(day_gold) | TextUtils.isEmpty(rank)) {
            applyMyGold(0);
        } else {
            MyGold myGold = new MyGold();
            myGold.today_income = day_gold;
            myGold.coins = total_gold;
            myGold.pos = rank;
            userGolds.clear();
            userGolds.add(myGold);
            adapter.notifyDataSetChanged();
            applyMyGold(1);
        }
    }

    /**
     * 初始化我的消息
     */
    private void initMessageData() {
        url = Link.PROJECT_MY_MESSAGE;
        tag = RequestTag.REQUEST_TAG_MY_MESSAGE;
        adapter = new ChannelPersonalAdapter(getActivity(), userMessages, channel);
        mRefreshRecyclerView.setAdapter(adapter);

        String resource3 = sp.getString(PersonalFragment.KEY_MESSAGE, "");
        if (TextUtils.isEmpty(resource3)) {
            applyData(0);
        } else {
            applyData(1);
            ArrayList<UserMessage> messages = GsonUtil.getInstance().fromJson(
                    resource3, new TypeToken<ArrayList<UserMessage>>() {
                    }.getType());
            userMessages.clear();
            userMessages.addAll(messages);
            adapter.notifyDataSetChanged();
            if (userMessages.size() == 0) {
                dealEmpty();
            } else {
                mEmpty.setVisibility(View.GONE);
                mEmpty1.setVisibility(View.GONE);
                mEmptyImage.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 初始化我的推荐数据
     */
    private void initRecommendData() {
        url = Link.PROJECT_MY_RECOMMEND;
        tag = RequestTag.REQUEST_TAG_MY_RECOMMEND;
        adapter = new ChannelPersonalAdapter(getActivity(), userRecommends, channel);
        mRefreshRecyclerView.setAdapter(adapter);

        String resource = sp.getString(PersonalFragment.KEY_RECOMMEND, "");
        L.i("------------empty------------" + resource);
        if (TextUtils.isEmpty(resource)) {
            applyData(0);
        } else {
            L.i("------------empty------------");
            applyData(1);
            ArrayList<UserRecommend> recommends = GsonUtil.getInstance().fromJson(
                    resource, new TypeToken<ArrayList<UserRecommend>>() {
                    }.getType());
            userRecommends.clear();
            userRecommends.addAll(recommends);
            adapter.notifyDataSetChanged();
            if (userRecommends.size() == 0) {
                dealEmpty();
            } else {
                mEmpty.setVisibility(View.GONE);
                mEmpty1.setVisibility(View.GONE);
                mEmptyImage.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 获取金币
     *
     * @param i
     */
    private void applyMyGold(int i) {
        String token = SPUtils.getToken(DemoApplication.applicationContect);
        if (TextUtils.isEmpty(token)) return;
        if (i == 0) progressDialog.show();
        HashMap<String, String> param = new HashMap<>();
        param.put("token", token);
        VolleyRequest.volleyRequestPost(Link.PROJECT_MY_COINS,
                RequestTag.REQUEST_TAG_MY_COINS, param, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object o) {
                        String result = o.toString();
                        if (progressDialog != null) progressDialog.dismiss();
                        L.i(result);
                        if (!TextUtils.isEmpty(result)) {
                            if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                                MyGold myGold = GsonUtil.getInstance().fromJson(GsonUtil.
                                        pareJSONObject(result).optJSONObject(RequestCode.KEY_ARRAY)
                                        .toString(), MyGold.class);
                                sp.edit().putString(PersonalFragment.KEY_TOTAL_GOLD, myGold.coins).commit();
                                sp.edit().putString(PersonalFragment.KEY_DAY_GOLD, myGold.today_income).commit();
                                sp.edit().putString(PersonalFragment.KEY_RANK, myGold.pos).commit();
                                userGolds.clear();
                                userGolds.add(myGold);
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getActivity(), GsonUtil.pareMsg(result), Toast.LENGTH_SHORT).show();
                                ErrorDealUtil.dealError(getActivity(), GsonUtil.pareCode(result));
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
    }

    /**
     * 请求数据
     *
     * @param type
     */
    public void applyData(int type) {
        page = 1;
        if (type == 0) progressDialog.show();

        String token = SPUtils.getToken(DemoApplication.applicationContect);
        if (TextUtils.isEmpty(token)) return;
        L.i("---------------" + page);
        HashMap<String, String> param = new HashMap<>();
        param.put("p", String.valueOf(page));
        param.put("token", token);
        VolleyRequest.volleyRequestPost(url, tag, param, new VolleyInterface() {
            @Override
            public void onSuccess(Object o) {
                String result = o.toString();
                progressDialog.dismiss();
                L.i("---------------result------------" + result);
                if (!TextUtils.isEmpty(result)) {
                    if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                        JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                        switch (channel) {
                            case PersonalFragment.CHANNEL_RECOMMEND:
                                sp.edit().putString(PersonalFragment.KEY_RECOMMEND, array.toString()).commit();
                                ArrayList<UserRecommend> recommends = GsonUtil.getInstance().fromJson(
                                        array.toString(), new TypeToken<ArrayList<UserRecommend>>() {
                                        }.getType());
                                userRecommends.clear();
                                userRecommends.addAll(recommends);
                                if (userRecommends.size() == 0) {
                                    dealEmpty();
                                } else {
                                    mEmpty.setVisibility(View.GONE);
                                }
                                adapter.notifyDataSetChanged();
                                break;
                            case PersonalFragment.CHANNEL_LIKE:
                                sp.edit().putString(PersonalFragment.KEY_LIKE, array.toString()).commit();
                                ArrayList<UserLike> likes = GsonUtil.getInstance().fromJson(
                                        array.toString(), new TypeToken<ArrayList<UserLike>>() {
                                        }.getType());
                                userLikes.clear();
                                userLikes.addAll(likes);
                                if (userLikes.size() == 0) {
                                    dealEmpty();
                                } else {
                                    mEmpty.setVisibility(View.GONE);
                                }
                                adapter.notifyDataSetChanged();
                                break;
                            case PersonalFragment.CHANNEL_MESSAGE:
                                sp.edit().putString(PersonalFragment.KEY_MESSAGE, array.toString()).commit();
                                ArrayList<UserMessage> messages = GsonUtil.getInstance().fromJson(
                                        array.toString(), new TypeToken<ArrayList<UserMessage>>() {
                                        }.getType());
                                userMessages.clear();
                                userMessages.addAll(messages);
                                if (userMessages.size() == 0) {
                                    dealEmpty();
                                } else {
                                    mEmpty.setVisibility(View.GONE);
                                }
                                adapter.notifyDataSetChanged();
                                mRefreshRecyclerView.refreshComplete();
                                break;
                        }

                    } else {
                        ErrorDealUtil.dealError(getActivity(), GsonUtil.pareCode(result));
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
            }
        });
    }

    private void dealEmpty() {
        mEmpty.setVisibility(View.VISIBLE);
        if (channel == PersonalFragment.CHANNEL_RECOMMEND) {
            mEmpty1.setVisibility(View.VISIBLE);
            mEmptyImage.setVisibility(View.GONE);
            mHint.setText("您还没有推荐项目哦!");
            mGoTo.setText("马上推荐项目");
            mGoTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AddProjectActivity.class);
                    intent.putExtra(NotifyAction.KEY_RECOMMEND_BROADCAST, true);
                    getActivity().startActivity(intent);
                }
            });
        }
        if (channel == PersonalFragment.CHANNEL_LIKE) {
            mEmpty1.setVisibility(View.VISIBLE);
            mEmptyImage.setVisibility(View.GONE);
            mHint.setText("您还没有收藏项目哦!");
            mGoTo.setText("去逛逛");
            final MainActivity mainActivity = (MainActivity) getActivity();
            mGoTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.switchFind();
                }
            });
        }
        if (channel == PersonalFragment.CHANNEL_MESSAGE) {
            mEmpty1.setVisibility(View.GONE);
            mEmptyImage.setVisibility(View.VISIBLE);
            mEmptyImage.setImageResource(R.mipmap.icon_empty_message);
        }
    }


    @Override
    public void onRefresh() {
        applyData(1);
    }

    @Override
    public void onLoadMore() {
        loadMore();
    }

    private void loadMore() {
        ++page;
        String token = SPUtils.getToken(DemoApplication.applicationContect);
        if (TextUtils.isEmpty(token)) return;

        HashMap<String, String> param = new HashMap<>();
        param.put("p", String.valueOf(page));
        param.put("token", token);
        VolleyRequest.volleyRequestPost(url, tag, param, new VolleyInterface() {
            @Override
            public void onSuccess(Object o) {
                String result = o.toString();
                L.i("---------------result------------" + result);
                if (!TextUtils.isEmpty(result)) {
                    if (GsonUtil.pareCode(result) == RequestCode.SUCCESS) {
                        JSONArray array = GsonUtil.pareJSONObject(result).optJSONArray(RequestCode.KEY_ARRAY);
                        switch (channel) {
                            case PersonalFragment.CHANNEL_RECOMMEND:
                                ArrayList<UserRecommend> recommends = GsonUtil.getInstance().fromJson(
                                        array.toString(), new TypeToken<ArrayList<UserRecommend>>() {
                                        }.getType());
                                userRecommends.addAll(recommends);
                                adapter.notifyDataSetChanged();
                                break;
                            case PersonalFragment.CHANNEL_LIKE:
                                ArrayList<UserLike> likes = GsonUtil.getInstance().fromJson(
                                        array.toString(), new TypeToken<ArrayList<UserLike>>() {
                                        }.getType());
                                userLikes.addAll(likes);
                                break;
                            case PersonalFragment.CHANNEL_MESSAGE:
                                ArrayList<UserMessage> messages = GsonUtil.getInstance().fromJson(
                                        array.toString(), new TypeToken<ArrayList<UserMessage>>() {
                                        }.getType());
                                userMessages.addAll(messages);

                                break;
                        }
                        adapter.notifyDataSetChanged();
                        mRefreshRecyclerView.loadMoreComplete();
                    } else {
                        ErrorDealUtil.dealError(getActivity(), GsonUtil.pareCode(result));
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }

    @Override
    public void onStop() {
        ProjectItemClickUtil.dismiss();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        /*if (null != broadcastReceiver) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
        if (null != recBroadcastReceiver) {
            getActivity().unregisterReceiver(recBroadcastReceiver);
        }
        if (null != updateUserInfoBroadcastReceiver) {
            getActivity().unregisterReceiver(updateUserInfoBroadcastReceiver);
        }
        if (null != goldBroadcastReceiver) {
            getActivity().unregisterReceiver(goldBroadcastReceiver);
        }
        if (null != userMessageBroadcastReceiver) {
            getActivity().unregisterReceiver(userMessageBroadcastReceiver);
        }*/
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private class LikeBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NotifyAction.ACTION_UPDATE_USER_LIKE)) {
                L.i("-------------------LikeBroadcastReceiver-----------------");
                applyData(1);
            }
        }
    }

    private class RecommendBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NotifyAction.ACTION_UPDATE_USER_RECOMMEND)) {
                applyData(1);
            }
        }
    }

    BroadcastReceiver updateUserInfoBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().endsWith(NotifyAction.ACTION_UPDATE_USER_INFO)) {
                //清空数据
                applyData(1);

            }
        }
    };

    private class GoldBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().endsWith(NotifyAction.ACTION_UPDATE_USER_GOLD)) {
                //清空数据
                applyMyGold(1);
            }
        }
    }

    private class UserMessageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NotifyAction.ACTION_USER_MESSAGE_CHANGE)) {
                applyData(1);
            }
        }
    }


    public class MyGold implements Serializable {
        public String pos;     //":"1"
        public String user_id;     //"808""
        public String coins;     //"12""
        public String today_income;     //"1"
    }


}
