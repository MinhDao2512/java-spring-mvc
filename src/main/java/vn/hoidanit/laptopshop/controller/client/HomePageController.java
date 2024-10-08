package vn.hoidanit.laptopshop.controller.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.Role;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.CartDetailService;
import vn.hoidanit.laptopshop.service.CartService;
import vn.hoidanit.laptopshop.service.OrderDetailService;
import vn.hoidanit.laptopshop.service.OrderService;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.RoleService;
import vn.hoidanit.laptopshop.service.UserService;
import vn.hoidanit.laptopshop.service.dto.UserDTO;
import vn.hoidanit.laptopshop.service.mapper.UserMapper;

@Controller
public class HomePageController {

    private final ProductService productService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final CartService cartService;
    private final CartDetailService cartDetailService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    public HomePageController(ProductService productService, UserService userService, UserMapper userMapper,
            PasswordEncoder passwordEncoder, RoleService roleService, CartService cartService,
            CartDetailService cartDetailService, OrderService orderService, OrderDetailService orderDetailService) {
        this.productService = productService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.cartService = cartService;
        this.cartDetailService = cartDetailService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product> products = this.productService.getAllProducts();
        model.addAttribute("products", products);
        return "client/homepage/show";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String postRegisterUser(Model model, @ModelAttribute("userDTO") @Valid UserDTO userDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            return "client/auth/register";
        }
        Role role = this.roleService.getByName("USER");
        User user = this.userMapper.userDTOToUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(role);
        this.userService.createOrUpdateUser(user);
        return "redirect:/login?successRegister";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "client/auth/login";
    }

    @GetMapping("/error/403")
    public String get403ErrorPage() {
        return "client/auth/403_error";
    }

    @GetMapping("/cart-detail")
    public String getCartDetailPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        User user = this.userService.getUserByEmail((String) session.getAttribute("email"));
        Cart cart = this.cartService.getCartByUser(user);
        if (cart != null) {
            List<CartDetail> cartDetails = this.cartDetailService.getCartDetailsByCart(cart);
            model.addAttribute("cartDetails", cartDetails);
        } else {
            session.setAttribute("sum", 0);
        }
        return "client/cart/show";
    }

    @GetMapping("/confirm-checkout")
    public String getCheckoutPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        User user = this.userService.getUserByEmail((String) session.getAttribute("email"));
        Cart cart = this.cartService.getCartByUser(user);
        if (cart != null) {
            List<CartDetail> cartDetails = this.cartDetailService.getCartDetailsByCart(cart);
            model.addAttribute("cartDetails", cartDetails);
        } else {
            session.setAttribute("sum", 0);
        }
        return "client/cart/checkout";
    }

    @GetMapping("/thanks")
    public String getThankYouPage() {

        return "client/cart/thanks";
    }

    @PostMapping("/place-order")
    public String postPlaceOrderProduct(
            HttpServletRequest request,
            @RequestParam("receiverName") String receiverName,
            @RequestParam("receiverAddress") String receiverAddress,
            @RequestParam("receiverPhone") String receiverPhone) {

        HttpSession session = request.getSession(false);
        this.orderService.placeOrder(receiverName, receiverAddress, receiverPhone, session);
        return "redirect:/thanks";
    }

    @GetMapping("/order-history")
    public String getOrderHistoryPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User user = this.userService.getUserByEmail((String) session.getAttribute("email"));
        List<Order> orders = this.orderService.getAllByUser(user);
        if (orders != null) {
            Map<Order, List<OrderDetail>> map = new HashMap<>();
            for (Order order : orders) {
                List<OrderDetail> orderDetails = this.orderDetailService.getAllByOrder(order);
                map.put(order, orderDetails);
            }
            model.addAttribute("mapData", map);
        }
        return "client/cart/order-history";
    }
}