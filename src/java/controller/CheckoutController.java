package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Cart;
import model.CartDAO; // <--- Import
import model.OrderDAO;
import model.UserDTO;

public class CheckoutController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "ViewCart.jsp"; 
        try {
            HttpSession session = request.getSession();
            
            // 1. Kiểm tra Giỏ hàng
            Cart cart = (Cart) session.getAttribute("CART");
            if (cart == null || cart.getCart() == null || cart.getCart().isEmpty()) {
                request.setAttribute("ERROR", "Your cart is empty. Cannot checkout!");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            // 2. Kiểm tra User
            UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // 3. Gọi DAO để lưu Order vào DB
            OrderDAO dao = new OrderDAO();
            boolean result = dao.insertOrder(user, cart);

            if (result) {
                // 4. Nếu thành công:
                
                // Xóa giỏ hàng trong Session
                session.removeAttribute("CART");
                
                // --- MỚI THÊM: DỌN SẠCH CART TRONG DB ---
                CartDAO cartDAO = new CartDAO();
                cartDAO.clearCart(user.getUserID());
                // -----------------------------------------
                
                url = "OrderSuccess.jsp";
                response.sendRedirect(url); 
                return; 
            } else {
                request.setAttribute("ERROR", "Checkout failed! Please try again.");
            }

        } catch (Exception e) {
            log("Error at CheckoutController: " + e.toString());
        }
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override protected void doGet(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {processRequest(r,p);}
    @Override protected void doPost(HttpServletRequest r, HttpServletResponse p) throws ServletException, IOException {processRequest(r,p);}
}