import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // When the user first visits — show the form
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>User Login</title></head><body>");
        out.println("<h2>User Login</h2>");
        out.println("<form method='post' action='LoginServlet'>");
        out.println("Username: <input type='text' name='username' required><br><br>");
        out.println("Password: <input type='password' name='password' required><br><br>");
        out.println("<input type='submit' value='Login'>");
        out.println("</form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // When form is submitted — validate credentials
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String uname = request.getParameter("username");
        String pass = request.getParameter("password");

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Login Result</title></head><body>");

        // Hardcoded login validation
        if ("admin".equals(uname) && "12345".equals(pass)) {
            out.println("<h2>Welcome, " + uname + "!</h2>");
            out.println("<p>Login successful ✅</p>");
        } else {
            out.println("<h3 style='color:red;'>Invalid username or password ❌</h3>");
            out.println("<a href='LoginServlet'>Try Again</a>");
        }

        out.println("</body></html>");
    }
}
