package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Repository
public class OrderRepository {

    HashMap<String,Order> orderMap=new HashMap<>();
    HashMap<String,DeliveryPartner> partnerMap=new HashMap<>();
    HashMap<String,String> orderToPartner=new HashMap<>();
    HashMap<String, List<String>> partnerToOrder=new HashMap<>();


    public void addOrder(Order order){
        orderMap.put(order.getId(),order);
    }

    public void addPartner(String partnerId) {
        partnerMap.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void addOrderToPartner(String orderId, String partnerId) {
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            orderToPartner.put(orderId,partnerId);

            List<String> curOrders=new ArrayList<>();
            if(partnerToOrder.containsKey(partnerId)){
                curOrders= partnerToOrder.get(partnerId);
            }
            curOrders.add(orderId);
            partnerToOrder.put(partnerId,curOrders);

            DeliveryPartner deliveryPartner=partnerMap.get(partnerId);
            deliveryPartner.setNumberOfOrders(curOrders.size());
        }
    }

    public Order getOrderById(String orderId) {
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
            return partnerMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return partnerToOrder.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerToOrder.get(partnerId);
    }

    public List<String> getAllOrders() {
        List<String> ans=new ArrayList<>();
        for(String id:orderMap.keySet()){
            ans.add(id);
        }
        return ans;
    }

    public Integer getCountOfUnassignedOrders() {
        return (Integer) (orderMap.size()-orderToPartner.size());
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int time, String partnerId) {
        int count=0;
        List<String> orderList=partnerToOrder.get(partnerId);
        for(String id:orderList){
            Order curOrder=getOrderById(id);
            int ordertime=curOrder.getDeliveryTime();
            if(ordertime>time) count++;
        }
        return count;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId) {
        int lastTime=0;
        List<String> orderList=partnerToOrder.get(partnerId);
        for(String id:orderList){
            int deliveryTime=getOrderById(id).getDeliveryTime();
            if(deliveryTime>lastTime) lastTime=deliveryTime;
        }
        return lastTime;
    }

    public void deletePartnerById(String partnerId) {
        partnerMap.remove(partnerId);

        List<String> orderList=partnerToOrder.get(partnerId);
        partnerToOrder.remove(partnerId);

        for(String id:orderList){
            orderToPartner.remove(id);
        }
    }

    public void deleteOrderById(String orderId) {
        orderMap.remove(orderId);

        String partnerId=orderToPartner.get(orderId);
        orderToPartner.remove(orderId);

        partnerToOrder.get(partnerId).remove(orderId);

        partnerMap.get(partnerId).setNumberOfOrders(partnerToOrder.get(partnerId).size());
    }
}
