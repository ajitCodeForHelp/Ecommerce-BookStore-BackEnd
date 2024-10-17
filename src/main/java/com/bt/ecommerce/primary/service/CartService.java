package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.bean.DataTableResponsePacket;
import com.bt.ecommerce.bean.KeyValueDto;
import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.AbstractDto;
import com.bt.ecommerce.primary.dto.CartDto;
import com.bt.ecommerce.primary.mapper.AddressMapper;
import com.bt.ecommerce.primary.mapper.ItemMapper;
import com.bt.ecommerce.primary.mapper.SystemUserMapper;
import com.bt.ecommerce.primary.pojo.Address;
import com.bt.ecommerce.primary.pojo.Cart;
import com.bt.ecommerce.primary.pojo.Item;
import com.bt.ecommerce.primary.pojo.user.SystemUser;
import com.bt.ecommerce.security.JwtUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CartService extends _BaseService implements _BaseServiceImpl {
    @Override
    public String save(AbstractDto.Save saveDto) throws BadRequestException {
        CartDto.CreateCart createCartDto = (CartDto.CreateCart) saveDto;
        SystemUser loggedInCustomer = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        //todo: Need to check if the cart exist for customer if yes then update the cart else create a new cart
        Cart cart = new Cart();
        //Customer Detail
        Cart.CustomerDetail customerCart = SystemUserMapper.MAPPER.mapToCartCustomer(loggedInCustomer);
        cart.setCustomerDetail(customerCart);
        // set the items in cart object
        List<Cart.ItemDetail> itemDetails = mapItemToCart(createCartDto.getItemUuids());
        cart.setItemDetailList(itemDetails);
        // check if we get the address uuid, if yes then map the address to the cart customer address
        if (createCartDto.getAddressUuid() != null) {
            mapAddressToCart(cart, createCartDto.getAddressUuid());
        }
        saveCartAmount(cart, itemDetails);
        cartRepository.save(cart);
        return cart.getUuid();
    }

    private void saveCartAmount(Cart cart, List<Cart.ItemDetail> itemDetails) {
        double cartSubTotal = 0;
        double packingCharges = 0;
        for (Cart.ItemDetail itemDetail : itemDetails) {
            cartSubTotal += itemDetail.getItemTotal() * itemDetail.getQuantity();
            packingCharges += 10 * itemDetail.getQuantity();
        }
        cart.setSubTotal(cartSubTotal);
        //>TODO: coupon code discount and delivery charges calculation and its implementations
        cart.setOrderTotal(cartSubTotal + packingCharges);
    }

    private void mapAddressToCart(Cart cart, String addressUuid) {
        Address address = addressRepository.findByUuid(addressUuid);
        Cart.CustomerAddressDetail customerAddressDetail = AddressMapper.MAPPER.mapToCartAddress(address);
        cart.setCustomerAddressDetail(customerAddressDetail);
    }

    private List<Cart.ItemDetail> mapItemToCart(List<String> itemUuids) {
        List<Cart.ItemDetail> itemDetails = new ArrayList<>();
        for (String itemUuid : itemUuids) {
            Item item = itemRepository.findByUuid(itemUuid);
            Cart.ItemDetail itemDetail = ItemMapper.MAPPER.mapToCartItem(item);
            itemDetails.add(itemDetail);
        }
        return itemDetails;
    }

    @Override
    public void update(String uuid, AbstractDto.Update updateDto) throws BadRequestException {
        CartDto.UpdateCart updateCart = (CartDto.UpdateCart) updateDto;
        Cart cart = cartRepository.findByUuid(uuid);
        if (cart == null) {
            cart = new Cart();
        }
        List<Cart.ItemDetail> itemDetails = mapItemToCart(updateCart.getItemUuids());
        cart.getItemDetailList().addAll(itemDetails);
        saveCartAmount(cart, cart.getItemDetailList());
        cartRepository.save(cart);
    }

    public void removeItemFromCart(String cartUuid, String itemUuid) {
        Cart cart = cartRepository.findByUuid(cartUuid);
        if (cart == null) {
            cart = new Cart();
        }
        List<Cart.ItemDetail> cartItemRemoved = cart.getItemDetailList().stream().filter(x -> !x.getItemUuid().equalsIgnoreCase(itemUuid)).toList();
        cart.setItemDetailList(cartItemRemoved);
        saveCartAmount(cart, cartItemRemoved);
        cartRepository.save(cart);
    }

    public void clearCart(String cartUuid) {
        Cart cart = cartRepository.findByUuid(cartUuid);
        if (cart == null) {
            cart = new Cart();
        }
        cart.setItemDetailList(new ArrayList<>());
        cart.setSubTotal(0.00);
        cart.setOrderTotal(0.00);
        cartRepository.save(cart);
    }

    public void updateItemQuantity(String cartUuid, String itemUuid, long quantity) {
        Cart cart = cartRepository.findByUuid(cartUuid);
        cart.getItemDetailList().stream().filter(x -> x.getItemUuid().equalsIgnoreCase(itemUuid)).findFirst().ifPresent(x -> x.setQuantity(quantity));
        saveCartAmount(cart, cart.getItemDetailList());
        cartRepository.save(cart);
    }

    @Override
    public AbstractDto.Detail get(String uuid) throws BadRequestException {
        return null;
    }

    @Override
    public DataTableResponsePacket list(Boolean deleted, Integer pageNumber, Integer pageSize, String search) {
        return null;
    }

    @Override
    public void activate(String uuid) throws BadRequestException {

    }

    @Override
    public void inactivate(String uuid) throws BadRequestException {

    }

    @Override
    public void delete(String uuid) throws BadRequestException {

    }

    @Override
    public void revive(String uuid) throws BadRequestException {

    }

    @Override
    public List<KeyValueDto> listInKeyValue() throws BadRequestException {
        return null;
    }

}
