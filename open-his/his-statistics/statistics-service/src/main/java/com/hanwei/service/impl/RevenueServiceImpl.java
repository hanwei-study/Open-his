package com.hanwei.service.impl;

import com.hanwei.constants.Constants;
import com.hanwei.domain.Income;
import com.hanwei.domain.Refund;
import com.hanwei.dto.RevenueQueryDto;
import com.hanwei.mapper.RevenueMapper;
import com.hanwei.service.RevenueService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RevenueServiceImpl implements RevenueService {


    @Autowired
    private RevenueMapper revenueMapper;

    @Override
    public Map<String, Object> queryAllRevenueData(RevenueQueryDto revenueQueryDto) {
        //最后的返回对象
        Map<String,Object> map=new HashMap<>();

        //查询收入的
        List<Income> incomes=this.revenueMapper.queryIncome(revenueQueryDto);
        //查询退费的
        List<Refund> refunds=this.revenueMapper.queryRefund(revenueQueryDto);

        //声明需要的数据对象
        //合计收入
        Double totalRevenue=0.00;
        //总收入
        Double toll=0.00;
        //总退费
        Double refund=0.00;
        //现金支付
        Double cashIncome=0.00;
        //现金退费
        Double cashRefund=0.00;
        //现金收取次数
        Integer incomeChanelCash=0;
        for (Income income : incomes) {
            toll+=income.getOrderAmount();
            //现金收费
            if(income.getPayType().equals(Constants.PAY_TYPE_0)){
                cashIncome+=income.getOrderAmount();
                incomeChanelCash++;
            }
        }
        for (Refund refund1 : refunds) {
            refund+=refund1.getBackAmount();
            //现金退费
            if(refund1.getBackType().equals(Constants.PAY_TYPE_0)){
                cashRefund+=refund1.getBackAmount();
            }
        }
        //计算合计收入=总收入-总退费
        totalRevenue=toll-refund;
        Map<String,Object> revenueObj=new HashMap<>();
        revenueObj.put("totalRevenue",totalRevenue);
        Map<String,Object> overview=new HashMap<>();
        overview.put("toll",toll);
        overview.put("refund",refund);

        Map<String,Object> channel=new HashMap<>();
        channel.put("cashIncome",cashIncome);
        channel.put("cashRefund", cashRefund);
        revenueObj.put("overview",overview);
        revenueObj.put("channel",channel);
        map.put("revenueObj",revenueObj);

        /*******收支概况***************/
        Map<String,Object> revenueOverview =new HashMap<>();
        revenueOverview.put("title","收支概况");
        List<Map<String,Object>> revenueOverviewData=new ArrayList<>();
        Map<String,Object> revenueOverviewData1=new HashMap<>();
        revenueOverviewData1.put("name","收费金额");
        revenueOverviewData1.put("value",toll);
        Map<String,Object> revenueOverviewData2=new HashMap<>();
        revenueOverviewData2.put("name","退费金额");
        revenueOverviewData2.put("value",refund);
        revenueOverviewData.add(revenueOverviewData1);
        revenueOverviewData.add(revenueOverviewData2);
        revenueOverview.put("data",revenueOverviewData);
        //放到返回的map里面
        map.put("revenueOverview",revenueOverview);

        /*******收入渠道***************/
        Map<String,Object> incomeChanel =new HashMap<>();
        incomeChanel.put("title","收入渠道");
        List<Map<String,Object>> incomeChanelData=new ArrayList<>();
        Map<String,Object> incomeChanelData1=new HashMap<>();
        incomeChanelData1.put("name","现金笔数");
        incomeChanelData1.put("value",incomeChanelCash);
        incomeChanelData.add(incomeChanelData1);
        incomeChanel.put("data",incomeChanelData);
        //放到返回的map里面
        map.put("incomeChanel",incomeChanel);

        /*******退款金额和渠道***************/
        Map<String,Object> refundMap =new HashMap<>();
        refundMap.put("title","退款");
        List<Map<String,Object>> refundMapData=new ArrayList<>();
        Map<String,Object> refundMapData1=new HashMap<>();
        refundMapData1.put("name","现金退款");
        refundMapData1.put("value",cashRefund);
        refundMapData.add(refundMapData1);
        refundMap.put("data",refundMapData);
        //放到返回的map里面
        map.put("refund",refundMap);
        return map;
    }
}
