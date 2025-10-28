import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class AttendanceServlet extends HttpServlet {

    // JDBC credentials
    String jdbcURL = "jdbc:mysql://localhost:3306/yourdb";  // Change DB name
    String jdbcUser = "root";                               // Change username
    String jdbcPass = "yourpassword";                       // Change password

    // Display Attendance Form (GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Student Attendance Portal</title></head><body>");
        out.println("<h2>Student Attendance Portal</h2>");

        // Form for attendance input
        out.println("<form action='AttendanceServlet' method='post'>");
        out.println("Student ID: <input type='text' name='studentId' required><br><br>");
        out.println("Date (YYYY-MM-DD): <input type='text' name='date' required><br><br>");
        out.println("Status: <select name='status'>");
        out.println("<option value='Present'>Present</option>");
        out.println("<option value='Absent'>Absent</option>");
        out.println("</select><br><br>");
        out.println("<input type='submit' value='Submit Attendance'>");
        out.println("</form>");

        // Option to view all records
        out.println("<br><br><form action='AttendanceServlet' method='get'>");
        out.println("<input type='hidden' name='action' value='view'>");
        out.println("<input type='submit' value='View All Attendance Records'>");
        out.println("</form>");

        // If user requested to view all attendance
        String action = request.getParameter("action");
        if ("view".equals(action)) {
            displayAttendance(out);
        }

        out.println("</body></html>");
    }

    // Handle Attendance Submission (POST)
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String studentId = request.getParameter("studentId");
        String date = request.getParameter("date");
        String status = request.getParameter("status");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass);

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Attendance (StudentID, Date, Status) VALUES (?, ?, ?)");
            ps.setInt(1, Integer.parseInt(studentId));
            ps.setString(2, date);
            ps.setString(3, status);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                out.println("<html><body>");
                out.println("<h3 style='color:green;'>Attendance Recorded Successfully!</h3>");
                out.println("<a href='AttendanceServlet'>Go Back</a>");
                out.println("</body></html>");
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            out.println("<html><body><h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
            out.println("<a href='AttendanceServlet'>Go Back</a>");
            out.println("</body></html>");
        }
    }

    // Method to display attendance table
    private void displayAttendance(PrintWriter out) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPass);

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Attendance");

            out.println("<h3>All Attendance Records</h3>");
            out.println("<table border='1'><tr><th>StudentID</th><th>Date</th><th>Status</th></tr>");

            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("StudentID") + "</td>"
                        + "<td>" + rs.getString("Date") + "</td>"
                        + "<td>" + rs.getString("Status") + "</td></tr>");
            }

            out.println("</table>");
            rs.close();
            st.close();
            con.close();

        } catch (Exception e) {
            out.println("<p style='color:red;'>Error displaying attendance: " + e.getMessage() + "</p>");
        }
    }
}
