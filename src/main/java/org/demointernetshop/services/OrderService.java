package org.demointernetshop.services;

import lombok.RequiredArgsConstructor;
import org.demointernetshop.dto.order.OrderDto;
import org.demointernetshop.dto.order.OrderRequestDto;
import org.demointernetshop.dto.product.ProductShortInfoDto;
import org.demointernetshop.entity.*;
import org.demointernetshop.repository.*;
import org.demointernetshop.services.utils.Converters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductInfoRepository productInfoRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final Converters converters;

    @Transactional
    public OrderDto createOrder(OrderRequestDto request) {
        Cart cart = cartRepository.findById(request.getCartId())
                .orElseThrow(() -> new RuntimeException("Cart not found for cart id: " + request.getCartId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found for user id: " + request.getUserId()));


        OrderStatus orderStatus = orderStatusRepository.findByStatus("processing")
                .orElseThrow(() -> new RuntimeException("OrderStatus not found by status: processing"));

        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId()).orElseThrow(
                () -> new RuntimeException("Payment Method not found by PaymentMethodId: " + request.getPaymentMethodId()));

        PaymentStatus paymentStatus = paymentStatusRepository.findByStatus("pending").orElseThrow(
                () -> new RuntimeException("Payment Status not found by Status: \"pending\""));
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setOrderStatus(orderStatus);
        newOrder.setPaymentMethod(paymentMethod);
        newOrder.setPaymentStatus(paymentStatus);
        newOrder.setCreateData(LocalDateTime.now());
        newOrder = orderRepository.save(newOrder);

        List<ProductShortInfoDto> productDtos = request.getProducts();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ProductShortInfoDto productFromRequest : productDtos) {
            ProductInfo productInfo = productInfoRepository.findByProduct_Id(productFromRequest.getId())
                    .orElseThrow(() -> new RuntimeException("ProductInfo not found for product id: " + productFromRequest.getId()));

            if (productInfo.getQuantity() < productFromRequest.getQuantity()) {
                new RuntimeException("Quantity is not enough for for product id: " + productFromRequest.getId());
            }

            if (productInfo.getPrice() != productFromRequest.getPrice()) {
                new RuntimeException("Price for product id: " + productFromRequest.getId() + " is different");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(productInfo.getProduct());
            orderItem.setQuantity(productFromRequest.getQuantity());
            orderItem.setPrice(productInfo.getPrice());
            orderItem.setOrder(newOrder);
            orderItemRepository.save(orderItem);

            productInfo.setQuantity(productInfo.getQuantity() - productFromRequest.getQuantity());
            productInfoRepository.save(productInfo);

            totalAmount = totalAmount.add(productInfo.getPrice().multiply(BigDecimal.valueOf(productFromRequest.getQuantity())));
        }

        newOrder.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(newOrder);

        // Clear the cart ???
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return converters.convertToOrderDto(savedOrder, user.getId(), productDtos);
    }

    public void deleteOrder(Integer orderId) {
        orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found for order id: " + orderId));

        orderRepository.deleteById(orderId);
    }
}
