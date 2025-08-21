package com.bt.ecommerce.primary.service;

import com.bt.ecommerce.configuration.SpringBeanContext;
import com.bt.ecommerce.exception.BadRequestException;
import com.bt.ecommerce.primary.dto.InventoryCartDto;
import com.bt.ecommerce.primary.mapper.InventoryCartMapper;
import com.bt.ecommerce.primary.mapper.ItemMapper;
import com.bt.ecommerce.primary.pojo.InventoryCart;
import com.bt.ecommerce.primary.pojo.Item;
import com.bt.ecommerce.primary.pojo.user.Customer;
import com.bt.ecommerce.security.JwtTokenUtil;
import com.bt.ecommerce.utils.TextUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryCartService extends _BaseService {

    public InventoryCartDto.DetailInventoryCart getInventoryCartDetail(String authorizationToken) throws BadRequestException {
        InventoryCart cart = getCartByStaffId(authorizationToken);
        return InventoryCartMapper.MAPPER.mapToDetailInventoryCartDto(cart);
    }

    public List<InventoryCartDto.DetailInventoryCart> getInventoryCartListForOrder(String authorizationToken) throws BadRequestException {
        Customer loggedInCustomer = null;
        try {
            if (!TextUtils.isEmpty(authorizationToken)) {
                String username = SpringBeanContext
                        .getBean(JwtTokenUtil.class)
                        .validateToken(authorizationToken);
                loggedInCustomer = customerRepository.findFirstByUsername(username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // todo check valid user for All Cart Access
        return inventoryCartRepository.findAll()
                .stream()
                .map(InventoryCartMapper.MAPPER::mapToDetailInventoryCartDto)
                .collect(Collectors.toList());
    }


    public InventoryCartDto.DetailInventoryCart updateCart(String authorizationToken, String cartUuid, InventoryCartDto.UpdateInventoryCart cartDto) throws BadRequestException {
        InventoryCart cart = inventoryCartRepository.findByUuid(cartUuid);
        if (cart == null) {
            cart = createNewInventoryCart(authorizationToken);
        }
        cart = mapToCartItem(cart, cartDto);
        cart = inventoryCartRepository.save(cart);
        return InventoryCartMapper.MAPPER.mapToDetailInventoryCartDto(cart);
    }

    public void clearCart(String cartUuid) {
        InventoryCart cart = inventoryCartRepository.findByUuid(cartUuid);
        if (cart == null) {
            return;
        }
        inventoryCartRepository.delete(cart);
    }

    public InventoryCartDto.DetailInventoryCart removeItemFromCart(String cartUuid, String itemUuid) throws BadRequestException {
        InventoryCart cart = inventoryCartRepository.findByUuid(cartUuid);
        if (cart == null) {
            throw new BadRequestException("There is no valid cart");
        }
        Item item = itemRepository.findByUuid(itemUuid);
        if (item == null) {
            throw new BadRequestException("Please provide a valid item");
        }
        if (cart.getItemIds().contains(item.getId())) {
            InventoryCart.ItemDetail removedItemDetail = null;
            for (InventoryCart.ItemDetail itemDetail : cart.getItemDetailList()) {
                if (itemDetail.getItemUuid().equals(itemUuid)) {
                    removedItemDetail = itemDetail;
                    break;
                }
            }
            // Item Removed
            cart.getItemIds().remove(item.getId());
            cart.getItemDetailList().remove(removedItemDetail);
        }
        inventoryCartRepository.save(cart);
        return InventoryCartMapper.MAPPER.mapToDetailInventoryCartDto(cart);
    }

    public InventoryCartDto.DetailInventoryCart updateItemQuantity(String cartUuid, String itemUuid, long quantity) throws BadRequestException {
        InventoryCart cart = inventoryCartRepository.findByUuid(cartUuid);
        if (cart == null) {
            throw new BadRequestException("Please provide a valid cart");
        }
        cart.getItemDetailList().stream()
                .filter(x -> x.getItemUuid().equalsIgnoreCase(itemUuid)).findFirst().ifPresent(x -> x.setQuantity(quantity));
        inventoryCartRepository.save(cart);
        return InventoryCartMapper.MAPPER.mapToDetailInventoryCartDto(cart);
    }

    private InventoryCart mapToCartItem(InventoryCart cart, InventoryCartDto.UpdateInventoryCart cartDto) throws BadRequestException {
        if (cartDto.getItemQuantityMap() != null && !cartDto.getItemQuantityMap().isEmpty()) {
            List<ObjectId> itemIds = new ArrayList<>();
            List<InventoryCart.ItemDetail> itemDetailList = new ArrayList<>();
            for (String itemUuid : cartDto.getItemQuantityMap().keySet()) {
                Item item = itemRepository.findByUuid(itemUuid);
                Long itemQuantity = cartDto.getItemQuantityMap().get(itemUuid);
                if (itemQuantity < 1) {
                    throw new BadRequestException("Please provide a valid item quantity");
                }
                if (item == null) {
                    throw new BadRequestException("Invalid item selection");
                }
                if (item.isDeleted() || !item.isActive()) {
                    throw new BadRequestException("Invalid item selection, Please select a active item");
                }
                // Map a new item To cart
                InventoryCart.ItemDetail itemDetail = ItemMapper.MAPPER.mapToInventoryCartItem(item);
                itemDetail.setQuantity(itemQuantity);
                itemIds.add(item.getId());
                itemDetailList.add(itemDetail);
            }
            cart.setItemIds(itemIds);
            cart.setItemDetailList(itemDetailList);
        }

        // To Add Item First Time in Cart
        if (cartDto.getItemUuid() != null) {
            Item item = itemRepository.findByUuid(cartDto.getItemUuid());
            if (item == null) {
                throw new BadRequestException("Invalid item selection");
            }
            if (item.isDeleted() || !item.isActive()) {
                throw new BadRequestException("Invalid item selection, Please select a active item");
            }
            InventoryCart.ItemDetail itemDetail = null;
            if (cart.getItemIds().contains(item.getId())) {
                for (InventoryCart.ItemDetail detail : cart.getItemDetailList()) {
                    if (item.getUuid().equals(detail.getItemUuid())) {
                        itemDetail = detail;
                        break;
                    }
                }
            } else {
                // Map a new item To cart
                itemDetail = ItemMapper.MAPPER.mapToInventoryCartItem(item);
                cart.getItemIds().add(item.getId());
            }
            cart.getItemDetailList().add(itemDetail);
        }
        return cart;
    }

    private InventoryCart createNewInventoryCart(String authorizationToken) throws BadRequestException {
        Customer loggedInCustomer = null;
        InventoryCart cart = null;
        try {
            if (!TextUtils.isEmpty(authorizationToken)) {
                String username = SpringBeanContext.getBean(JwtTokenUtil.class).validateToken(authorizationToken);
                loggedInCustomer = customerRepository.findFirstByUsername(username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loggedInCustomer != null) {
            cart = inventoryCartRepository.findByCustomerId(loggedInCustomer.getId());
        }

        if (cart == null) {
            cart = new InventoryCart();
        }

        if (loggedInCustomer != null) {
            cart.setCustomerId(loggedInCustomer.getId());
            cart.setCustomerDetail(new InventoryCart.StaffRefDetail(
                    loggedInCustomer.getUuid(), loggedInCustomer.getFirstName(),
                    loggedInCustomer.getLastName()));
        }
        cart = inventoryCartRepository.save(cart);
        if (cart == null) {
            throw new BadRequestException("Invalid DeviceId Provided And Authorization");
        }
        return cart;
    }
    private InventoryCart getCartByStaffId(String authorizationToken) throws BadRequestException {
        Customer loggedInCustomer = null;
        InventoryCart cart = null;
        try {
            if (!TextUtils.isEmpty(authorizationToken)) {
                String username = SpringBeanContext.getBean(JwtTokenUtil.class).validateToken(authorizationToken);
                loggedInCustomer = customerRepository.findFirstByUsername(username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loggedInCustomer != null) {
            cart = inventoryCartRepository.findByCustomerId(loggedInCustomer.getId());
        }
        if (cart == null) {
            cart = createNewInventoryCart(authorizationToken);
        }
        return cart;
    }

    public InventoryCartDto.DetailInventoryCart get(String cartUuid) throws BadRequestException {
        InventoryCart cart = inventoryCartRepository.findByUuid(cartUuid);
        if (cart == null) {
            throw new BadRequestException("Invalid cart id provided");
        }
        return InventoryCartMapper.MAPPER.mapToDetailInventoryCartDto(cart);
    }
}
