package com.infosys.infytel.service;

 
import java.util.ArrayList;  
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infosys.infytel.controller.ProductFeign;
import com.infosys.infytel.controller.UserFeign;
import com.infosys.infytel.dto.BuyerDTO;
import com.infosys.infytel.controller.ProductFeign;
import com.infosys.infytel.controller.UserFeign;
import com.infosys.infytel.dto.CartDTO;
import com.infosys.infytel.dto.OrderDTO;
import com.infosys.infytel.dto.orderstatus;
import com.infosys.infytel.dto.productDTO;
import com.infosys.infytel.entity.Order;
import com.infosys.infytel.entity.ProductsOrdered;
import com.infosys.infytel.repository.OrderRepository;
import com.infosys.infytel.repository.ProductsOrderedRepository;

@Service
public class OrderService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@PersistenceContext
	private EntityManager entm;
	@Autowired
	OrderRepository oRepo;
	@Autowired
	UserFeign item;
	@Autowired
	ProductFeign pr;
	@Autowired
	ProductsOrderedRepository po;

	public String placeOrder(String buyerId,String address) throws Exception {
			Order od=oRepo.findTopByOrderByOrderIdDesc();
			String rt=od.getOrderId().substring(1);
			Integer st=Integer.parseInt(rt);
			String orderid="";
			if(address.length()>0 && address.length()<=100) throw new Exception("Service.ADDRESS_SHOULD_BE_OF_LENGTH_100");  
			List<CartDTO> pm=item.getCartDetails(buyerId);
			System.out.println(pm);
		    for(CartDTO pf : pm){
		    	ProductsOrdered pd=new ProductsOrdered();
		       	productDTO p=pr.getProduct(pf.getProdId());
		       	Order o=new Order();
		       	st++;
		       	orderid="O"+String.valueOf(st);
		       	o.setOrderId(orderid);
		       	o.setProdId(pf.getProdId());
		       	o.setBuyerId(pf.getBuyerId());
		       	buyerId=pf.getBuyerId();
		       	o.setDate(new Date());
		       	o.setAmount(pf.getQuantity()*p.getPrice());
		       	o.setAddress(address);
		       	o.setStatus(orderstatus.OrderPlaced);
		       	oRepo.save(o);
	        	pd.setProdId(p.getProdId());
	        	pd.setBuyerId(pf.getBuyerId());
		        pd.setSellerId(p.getSellerId());
		       	pd.setQuantity(pf.getQuantity());
		       	po.save(pd);
		    }
		    String sm=item.deleteAllCartItem(buyerId);   
		    return buyerId;
		}
	 public void updateStatus(orderstatus status,String orderId) throws Exception{
		 Optional<Order> od=oRepo.findById(orderId);
		 Order o=od.orElseThrow(()->new Exception("Service.ORDERID_NOT_FOUND"));
		 o.setStatus(status);
	 }
	 public OrderDTO viewOrder(String orderId) throws Exception {
			Optional<Order> optional = oRepo.findById(orderId);
			Order order = optional.orElseThrow(()->new Exception("Service.ORDERID_NOT_FOUND"));
			OrderDTO orderDTO = new OrderDTO();
			orderDTO.setOrderId(order.getOrderId());
			orderDTO.setBuyerId(order.getBuyerId());
			orderDTO.setAmount(order.getAmount());
			orderDTO.setAddress(order.getAddress());
			orderDTO.setDate(order.getDate());
			orderDTO.setStatus(order.getStatus());		
			return orderDTO;
	}
	 public List<OrderDTO> viewOrdersByBuyer(String buyerId) throws Exception {
		 	BuyerDTO bd=item.findBuyer(buyerId);
		 	if(bd==null) throw new Exception("Service.BUYER_NOT_FOUND");
		 	List<Order> orders =  oRepo.findAll();
			List<OrderDTO> dtoList = new ArrayList<>();
			for(Order or :orders) {
				if(or.getBuyerId().equals(buyerId)) {
					OrderDTO odto = new OrderDTO();
					odto.setOrderId(or.getOrderId());
					odto.setBuyerId(or.getBuyerId());
					odto.setAmount(or.getAmount());
					odto.setAddress(or.getAddress());
					odto.setDate(or.getDate());
					odto.setStatus(or.getStatus());
					dtoList.add(odto);
				}
			}
			if(dtoList.isEmpty()) throw new Exception("SERVICE.NO_ORDERS_FOUND_ON_BUYERID");
			return dtoList;
		}

	 public String reOrder(String OrderId,Integer quantity) throws Exception {
		 Optional<Order> optional = oRepo.findById(OrderId);
		 Order od=oRepo.findTopByOrderByOrderIdDesc();
		 
			String rt=od.getOrderId().substring(1);
			Integer st=Integer.parseInt(rt);
			String orderid="";
			st++;
	       	orderid="O"+String.valueOf(st);
			Order order = optional.orElseThrow(()->new Exception("Order does not exist for the given buyer"));
			Order reorder = new Order();
			productDTO p=pr.getProduct(order.getProdId());
			reorder.setOrderId(orderid);
			reorder.setProdId(order.getProdId());
			reorder.setBuyerId(order.getBuyerId());
			reorder.setAmount(p.getPrice()*quantity);
			reorder.setAddress(order.getAddress());
			reorder.setDate(new Date());
			reorder.setStatus(orderstatus.OrderPlaced);
			oRepo.save(reorder);
			List<ProductsOrdered> pd=po.findAll();
			for(ProductsOrdered pr:pd) {
				if(pr.getBuyerId().equals(order.getBuyerId()) && pr.getProdId().equals(order.getProdId())) {
					pr.setQuantity(pr.getQuantity()+quantity);
				}
			}
			return reorder.getOrderId();
	 }
}