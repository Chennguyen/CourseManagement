package controller;

import java.io.IOException;
import java.util.List; // Import List
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Cart;
import model.CartDAO;
import model.OrderDAO;
import model.UserDTO;

public class CheckoutController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "ViewCart.jsp";
        try {
            HttpSession session = request.getSession();
            
            // 1. Check Cart
            Cart cart = (Cart) session.getAttribute("CART");
            if (cart == null || cart.getCart() == null || cart.getCart().isEmpty()) {
                request.setAttribute("ERROR", "Your cart is empty. Cannot checkout!");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            // 2. Check User
            UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // 3. Insert Order
            OrderDAO dao = new OrderDAO();
            boolean result = dao.insertOrder(user, cart);

            if (result) {
                // --- XỬ LÝ THÀNH CÔNG ---
                
                // 3.1. Clear Cart
                session.removeAttribute("CART");
                CartDAO cartDAO = new CartDAO();
                cartDAO.clearCart(user.getUserID());
                
                // 3.2. Lấy danh sách License vừa tạo để hiển thị
                List<String> licenses = dao.getNewestLicenseKeys(user.getUserID());
                request.setAttribute("LICENSES", licenses); // Gửi sang JSP
                
                // 3.3. Chuyển trang (Dùng FORWARD để giữ dữ liệu attribute)
                url = "OrderSuccess.jsp";
                request.getRequestDispatcher(url).forward(request, response);
                return; 
            } else {
                request.setAttribute("ERROR", "Checkout failed! Please try again.");
            }

        } catch (Exception e) {
            log("Error at CheckoutController: " + e.toString());
        }
        // Nếu lỗi thì quay về ViewCart
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override protected void doGet(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {processRequest(r,p);}
    @Override protected void doPost(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {processRequest(r,p);}
}