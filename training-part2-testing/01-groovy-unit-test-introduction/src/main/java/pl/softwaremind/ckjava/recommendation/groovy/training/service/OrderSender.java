package pl.softwaremind.ckjava.recommendation.groovy.training.service;

import pl.softwaremind.ckjava.recommendation.groovy.training.domain.Order;

public interface OrderSender {

    void sendOrder(String to, Order order);

}
