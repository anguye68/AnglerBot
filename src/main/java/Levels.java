import java.sql.*;

public class Levels {

    /**
     * Establishes a connection with the sqlite db and returns that connection.
     */
    public static Connection connect() {
        Connection conn = null;
        try {
            // Registers the JDBC drivers.
            //Class.forName("org.sqlite.JDBC");
            //DriverManager.registerDriver(new org.sqlite.JDBC());
            conn = DriverManager.getConnection("jdbc:sqlite:angler.db");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Connection to SQLite has been established.");
        System.out.println(conn);
        return conn;
    }

    /**
     * Returns the required experience for a specific level.
     *
     * @param level level to return the experiencce required for.
     * @return integer representing the experience required for the level.
     */
    public static int getExpReq(int level) {
        return (int) Math.rint((Math.log(level)/Math.log(2)) * (30*level)) + 100;
    }

    /**
     * Returns an integer representing the user's level given the total experience.
     *
     * @param exp user's total experience
     * @return integer representing the level.
     */
    public static int getLevel(int exp) {
        int remainingExp = exp;
        int level = 0;

        while (remainingExp >= getExpReq(level)) {
            remainingExp -= getExpReq(level);
            level += 1;
        }

        return level;
    }

    /**
     * Retrieves the users current placement relative to all members with > 0 experience in the guild.
     *
     * @param id user's discord ID.
     * @return integer representing user's placement.
     */
    public static int getRank(String id) {

        String sql = "SELECT UID,EXP FROM users ORDER BY EXP DESC";
        boolean found = false;
        int rank = 0;

        try {
            Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next() && !found) {
                if (rs.getString("UID").equals(id)) {
                    found = true;
                } else {
                    rank++;
                }
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (rank == 0) {
            rank = 1;
        }

        return rank;
    }

    /**
     * Retrieves the total amount of users in the guild with > 0 experience.
     *
     * @return total number of users in the guild with > 0 experience.
     */
    public static int getTotalMembers() {
        String sql = "SELECT count(*) FROM users";
        int totalUsers = 0;

        try {
            Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            totalUsers = rs.getInt("count(*)");
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return totalUsers;
    }

    /**
     * Checks whether the user speaking already exists in the database.
     *
     * @param id user's discord ID.
     * @return boolean whether or not the user exists.
     */
    public static boolean userExists(String id) {

        boolean exists = false;
        String sql = "SELECT count(*) FROM users WHERE UID = ?";

        try {
            Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boolean found = rs.getBoolean(1);
                    if (found) {
                        exists = true;
                    }
                }
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return exists;
    }

    /**
     * Gets the users current TOTAL experience.
     *
     * @param id user's discord ID.
     * @return integer representing the user's total current experience.
     */
    public static int getCurrentExp(String id) {
        String sql = "SELECT EXP FROM users WHERE UID = ?";
        int currentExp = 0;

        try {
            Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            currentExp = rs.getInt("EXP");
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return currentExp;
    }

    /**
     * Adds a new user to the database that doesn't currently exist.
     *
     * @param id user's discord ID.
     * @param exp experience earned from user's first post.
     */
    public static void addUser(String id, int exp) {
        String sql = "INSERT INTO users(UID,EXP) VALUES(?,?)";

        try {
            Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ps.setInt(2, exp);

            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates an existing user in the database.
     *
     * @param id user's discord ID.
     * @param exp new experience value.
     */
    public static void updateUser(String id, int exp) {
        String sql = "UPDATE users SET exp = ? WHERE UID = ?";

        try {
            Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, exp);
            ps.setString(2, id);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
