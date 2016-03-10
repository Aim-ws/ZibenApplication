package com.ziben365.ocapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ziben365.ocapp.adapter.FlipAdapter;
import com.ziben365.ocapp.model.FlipCard;
import com.ziben365.ocapp.widget.flip.FlipViewController;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p>
 * Created by Administrator
 * on 2015/12/29.
 * email  1956766863@qq.com
 * <p>
 * 翻页效果
 */
public class FlipFragment extends Fragment {

    FlipViewController mFlipView;

    private List<FlipCard> mData = new ArrayList<>();
    private FlipAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFlipView = new FlipViewController(getActivity(),FlipViewController.HORIZONTAL);
        return mFlipView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mData.clear();


        adapter = new FlipAdapter(getActivity(),mData);
        mFlipView.setAdapter(adapter);
        mFlipView.setFlipByTouchEnabled(true);
        mFlipView.setOnViewFlipListener(new FlipViewController.ViewFlipListener() {
            @Override
            public void onViewFlipped(View view, int position) {
                if (position == adapter.getCount() - 1){
                    mData.addAll(addAll());
                    adapter.notifyDataSetChanged();
                }
            }
        });
        mData.addAll(addAll());
        adapter.notifyDataSetChanged();
    }

    /**
     * 添加数据
     */
    private List<FlipCard> addAll() {
        ArrayList<FlipCard> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            FlipCard flipCard = new FlipCard();
            switch (i){
                case 0:
                    flipCard.content = "http://www.zaobao.com/sites/default/files/styles/large/public/images/201512/20151229/ZB_29_12_2015_CJ_13_29857182_29855594_xufg_x8lian.jpg";
                    flipCard.imgUrl = "回顾2015年中国外交，接受本报采访的学者一致认为，中美的政治互信下滑，战略竞赛与对立态势加剧。学者也指出，南中国海问题突然成了中美关系的中心话题。新加坡邻近海域成了大国博弈的中心区，迫使我们更关注地缘政治格局的变化。所幸中美发生军事冲突的可能性很低，但涉及南中国海问题的矛盾加剧，几乎难以避免。\n" +
                            "\n" +
                            "本报昨天刊登中外学者点评总结2015年中国对外关系发展的上篇，今天刊登的是下篇。";
                    flipCard.name = "南中国海成中美关系中心话题";
                    flipCard.id = adapter.getCount() + i;
                    break;
                case 1:
                    flipCard.content = "深圳崩塌事故的调查与追责工作正在进行中，批准涉事渣土受纳场成立的光明新区城管局原局长徐远安在27日（周日）坠楼身亡。\n" +
                            "\n" +
                            "另外，深圳公安局官方微博昨日发布消息称，已对深圳市益相龙投资发展有限公司等企业负责人及滑坡事故相关责任人共12人采取了强制措施，下一步，深圳警方将配合国务院调查组，对涉嫌违法犯罪人员依法查处。\n" +
                            "\n" +
                            "据深圳市公安局南山分局昨日在官方微博上发布的消息，公安机关27日21时接报，在深圳市南山区某小区有人坠楼，警方赶往现场勘查和调查走访之后，查明坠楼者系自杀身亡，排除他杀，死者身份为光明新区城管局原局长徐某某。\n" +
                            "\n" +
                            "徐远安所在的光明新区是深圳崩塌事故的事发地，他被指有重大的事故责任嫌疑，是被调查的对象之一。\n" +
                            "\n" +
                            "就在徐远安跳楼的当日，中共深圳市委机关报《深圳特区报》通过官方微博发布消息，称事发原因初步断定是临时余泥渣土受纳场违规作业，受纳泥浆漫溢，冲出山体，冲毁房屋，冲进靠近山体的恒泰裕工业园。微博中指出，该受纳场是原光明新区城市管理局局长徐远安等人批准。";
                    flipCard.imgUrl = "http://www.zaobao.com/sites/default/files/styles/large/public/images/201512/20151229/zb_29_12_2015_cj_13_30352962_30346161_xufg_x8_ho.jpg";
                    flipCard.name = "批准渣土受纳场成立 深圳光明新区前官员跳楼自杀";
                    flipCard.id = adapter.getCount() + i;
                    break;
                case 2:
                    flipCard.content = "拉马迪是逊尼派占多数的安巴尔省首府。伊拉克政府军已经包围拉马迪几个星期，并在前天发动总攻。政府军发言人说，他们已经控制了位于拉马迪市中心的政府大楼，下一步是清除市内的伊国组织残余势力。";
                    flipCard.imgUrl = "http://www.zaobao.com/sites/default/files/styles/large/public/images/201512/20151229/ZB_29_12_2015_CJ_16_30349897_30347461_zhangmd_Jo_x8.jpg";
                    flipCard.name = "对伊国组织作战首次取得重大胜利 伊拉克政府军夺回省府城市拉马迪";
                    flipCard.id = adapter.getCount() + i;
                    break;
                case 3:
                    flipCard.content = "（柏林综合电）乌克兰战争、希腊债务危机，以及大规模的难民潮，2015年对欧洲而言可说是充满危机的一年，这些危机也让德国总理默克尔成为了欧洲的“实际领导人”。\n" +
                            "\n" +
                            "欧美多家媒体机构包括法新社、《时代》周刊和《金融时报》都把默克尔封为2015年全球最具影响力的人物。\n" +
                            "\n" +
                            "《纽约时报》专栏作家科恩甚至将她誉为“相等于、甚至超越”德国前总理阿登纳、施密特和科尔的“杰出欧洲领导人”。";
                    flipCard.imgUrl = "http://www.zaobao.com/sites/default/files/styles/large/public/images/201512/20151229/ZB_29_12_2015_CJ_16_30352828_30224220_zhangmd_x8_ck.jpg";
                    flipCard.name = "欧美多家媒体机构评选 默克尔获评今年全球最具影响力";
                    flipCard.id = adapter.getCount() + i;
                    break;
                case 4:
                    flipCard.content = "我并非是唯一这样的金融学教授：在为学生设定文论主题时，会提出诸如“在你看来，全球金融危机的主因是政府对金融市场的干预太多还是太少？”这样的问题。在面对这类非此即彼的问题时，我最近带的一批学生分成了三个阵营。\n" +
                            "\n" +
                            "大概有三分之一的学生被有效市场假说的故弄玄虚所折服，他们认为政府有原罪。政府考虑不周的干预扭曲了市场激励，特别是美国政府支持的抵押承销商房利美和房地美，以及社区再投资法（Community Reinvestment Act）。有些人甚至信奉美国自由派罗恩·保罗（Ron Paul）的观点，批判美联储作为最后贷款人的存在。\n" +
                            "\n" +
                            "另有三分之一的学生的政治立场正好相反，他们认为美联储前主席艾伦·格林斯潘（Alan Greenspan）是罪魁祸首。格林斯潘以不愿意干预金融市场而闻名，即使在杠杆大幅提高、资产价格看起来早已脱离现实支撑的情况下也是如此，这才是问题的根源。更广泛地说，西方政府的象征性监管方针导致了市场在本世纪初失控。\n" +
                            "\n" +
                            "剩下三分之一学生则两可皆有，认为政府在某些方面干预过多，在另一些方面干预过少。回避问题可不是通过考试的好办法；但学生们也许已经做过了深入思考。";
                    flipCard.imgUrl = "http://www.12306.cn/mormhweb/dongchewopu/l02.jpg";
                    flipCard.name = "霍华德·戴维斯：金融危机的政治后果";
                    flipCard.id = adapter.getCount() + i;
                    break;
                default:
                    flipCard.content = "（东京讯）中国在南中国海修建人工岛声索主权导致区域局势紧张，日本对此加以指责，但英国《金融时报》报道，日本事实上也正以大量移殖珊瑚的手法在扩充一座无人岩礁，意图主张这是自然形成的岛屿，据此向联合国提出专属经济区要求。\n" +
                            "\n" +
                            "冲之鸟岛（Okinotorishima）是一个无人珊瑚岛，在东京岸外约1700多公里的太平洋中。该岛大部分面积位于海平面之下，只有两个较大岩礁即使是在涨潮时也会露于海面之上。\n" +
                            "\n" +
                            "日本已投入8亿余元\n" +
                            "\n" +
                            "日本称，这是一个岛屿，应当有它自己的专属经济区，但中国坚称这只是一块岩礁。过去30年，日本政府已投入了6亿多美元（约8亿4360万新元）来防止其受到海平面上升的影响而完全被海水覆盖。\n" +
                            "\n" +
                            "日本海洋深层水研究所设在冲绳群岛的久米岛（Kumejima）上的温室，就负责培育扩充冲之鸟岛所需的珊瑚。工作人员把冲之鸟岛上的珊瑚移殖过来，然后在实验室里加以培植，希望让它们在实验室里生长大约一年之后移殖回冲之鸟岛。\n" +
                            "\n" +
                            "对日本来说，扩充冲之鸟岛有两大好处：研究人员希望为全球珊瑚礁的快速消失寻找解决方法，日本政府则希望保留它在该地区的仅有陆地，并据此在该地区划定200海里的专属经济区，以及加强其对该区主权的控制。";
                    flipCard.imgUrl = "http://www.zaobao.com/sites/default/files/styles/large/public/images/201512/20151229/ZB_29_12_2015_CJ_00_30353541_30352988_yinlu_yinlu_x8_jo.jpg";
                    flipCard.name = "曾批中国在南中国海造岛 英媒：日本移殖珊瑚扩充无人岩礁";
                    flipCard.id = adapter.getCount() + i;
                    break;
            }
            data.add(flipCard);
        }
        return data;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFlipView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mFlipView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
