package com.ziben365.ocapp.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.ziben365.ocapp.widget.wheel.model.CityModel;
import com.ziben365.ocapp.widget.wheel.model.DistrictModel;
import com.ziben365.ocapp.widget.wheel.model.ProvinceModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * This is a built-in template. It contains a code fragment that can be included into file templates (Templates tab) with the help of the
 * <p/>
 * Created by Administrator
 * on 2016/1/8.
 * email  1956766863@qq.com
 */
public class AreaUtils {
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";



    public AreaUtils(Context context){
        initProvinceDatas(context);
    }

    public String[] getProvinceData(){
        return mProvinceDatas;
    }
    public Map<String, String[]> getCityData(){
        return mCitisDatasMap;
    }
    public Map<String, String[]> getDistrictData(){
        return mDistrictDatasMap;
    }

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas(Context context) {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            // */ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0)
                            .getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            // */
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j)
                            .getDistrictList();
                    String[] distrinctNameArray = new String[districtList
                            .size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList
                            .size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(
                                districtList.get(k).getName(), districtList
                                .get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(),
                                districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    public class XmlParserHandler extends DefaultHandler {

        /**
         * 存储所有的解析对象
         */
        private List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();

        public XmlParserHandler() {

        }

        public List<ProvinceModel> getDataList() {
            return provinceList;
        }

        @Override
        public void startDocument() throws SAXException {
            // 当读到第一个开始标签的时候，会触发这个方法
        }

        ProvinceModel provinceModel = new ProvinceModel();
        CityModel cityModel = new CityModel();
        DistrictModel districtModel = new DistrictModel();

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            // 当遇到开始标记的时候，调用这个方法
            if (qName.equals("province")) {
                provinceModel = new ProvinceModel();
                provinceModel.setName(attributes.getValue(0));
                provinceModel.setCityList(new ArrayList<CityModel>());
            } else if (qName.equals("city")) {
                cityModel = new CityModel();
                cityModel.setName(attributes.getValue(0));
                cityModel.setDistrictList(new ArrayList<DistrictModel>());
            } else if (qName.equals("district")) {
                districtModel = new DistrictModel();
                districtModel.setName(attributes.getValue(0));
                districtModel.setZipcode(attributes.getValue(1));
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            // 遇到结束标记的时候，会调用这个方法
            if (qName.equals("district")) {
                cityModel.getDistrictList().add(districtModel);
            } else if (qName.equals("city")) {
                provinceModel.getCityList().add(cityModel);
            } else if (qName.equals("province")) {
                provinceList.add(provinceModel);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
        }

    }
}
