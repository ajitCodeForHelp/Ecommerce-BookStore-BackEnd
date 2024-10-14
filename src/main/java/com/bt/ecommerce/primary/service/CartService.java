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
        CartDto.createCart createCartDto = (CartDto.createCart) saveDto;
        SystemUser loggedInCustomer = (SystemUser) SpringBeanContext.getBean(JwtUserDetailsService.class).getLoggedInUser();
        //todo: Need to check if the cart exist for customer if yes then delete the previous cart.
        Cart cart = new Cart();
        //Customer Detail
        Cart.CustomerDetail customerCart = SystemUserMapper.MAPPER.mapToCartCustomer(loggedInCustomer);
        cart.setCustomerDetail(customerCart);
        // set the items in cart object
        mapItemToCart(cart, createCartDto.getItemUuids());
        // check if we get the address uuid, if yes then map the address to the cart customer address
        if (createCartDto.getAddressUuid() != null) {
            mapAddressToCart(cart, createCartDto.getAddressUuid());
        }
        setCartAmount(cart);
        cartRepository.save(cart);
        return cart.getUuid();
    }

    private void setCartAmount(Cart cart) {
        double cartSubTotal = 0;
        double packingCharges = 0;
        for (Cart.ItemDetail itemDetail : cart.getItemDetailList()) {
            cartSubTotal += itemDetail.getItemTotal();
            packingCharges += 10;
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

    private void mapItemToCart(Cart cart, List<String> itemUuids) {
        List<Cart.ItemDetail> itemDetails = new ArrayList<>();
        for (String itemUuid : itemUuids) {
            Item item = itemRepository.findByUuid(itemUuid);
            Cart.ItemDetail itemDetail = ItemMapper.MAPPER.mapToCartItem(item);
            itemDetails.add(itemDetail);
        }
        cart.setItemDetailList(itemDetails);
    }

    @Override
    public void update(String uuid, AbstractDto.Update updateDto) throws BadRequestException {

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
