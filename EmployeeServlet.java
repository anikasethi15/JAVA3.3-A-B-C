import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class EmployeeServlet extends HttpServlet {

    // Change these details as per your DB setup
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "password";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Show simple search form
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Employee Records</title></head><body>");
        out.println("<h2>Search Employee Records</h2>");
        out.println("<form method='post' action='EmployeeServlet'>");
        out.println("Enter Employee ID (leave blank for all): <input type='text' name='empid'>");
        out.println("<input type='submit' value='Search'>");
        out.println("</form><hr>");
        out.println("</body></html>");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String empId = request.getParameter("empid");

        out.println("<html><head><title>Employee Data</title></head><body>");
        out.println("<h2>Employee Records</h2>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            PreparedStatement ps;
            if (empId != null && !empId.trim().isEmpty()) {
                ps = con.prepareStatement("SELECT * FROM Employee WHERE EmpID = ?");
                ps.setInt(1, Integer.parseInt(empId));
            } else {
                ps = con.prepareStatement("SELECT * FROM Employee");
            }

            ResultSet rs = ps.executeQuery();
            boolean found = false;

            out.println("<table border='1' cellpadding='8'>");
            out.println("<tr><th>EmpID</th><th>Name</th><th>Salary</th></tr>");
            while (rs.next()) {
                found = true;
                out.println("<tr>");
                out.println("<td>" + rs.getInt("EmpID") + "</td>");
                out.println("<td>" + rs.getString("Name") + "</td>");
                out.println("<td>" + rs.getDouble("Salary") + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");

            if (!found) {
                out.println("<p style='color:red;'>No records found!</p>");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace(out);
        }

        out.println("<br><a href='EmployeeServlet'>Back to Search</a>");
        out.println("</body></html>");
    }
}
