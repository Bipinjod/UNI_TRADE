package com.unitrade.dao;

import com.unitrade.model.RememberToken;
import com.unitrade.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * RememberTokenDAO - Data Access Object for remember_tokens table.
 * Handles persistence for the "Remember Me" persistent login feature.
 *
 * Token design (split-token security pattern):
 *   - selector   : stored in DB and sent in cookie (used to look up the row)
 *   - validator  : raw value sent in cookie; only a BCrypt hash is stored in DB
 *
 * Cookie format stored on client: "selector:validatorRaw"
 */
public class RememberTokenDAO {

    /**
     * Save a new remember token to the database.
     *
     * @param token populated RememberToken (userId, selector, validatorHash, expiresAt)
     * @return true if saved successfully
     */
    public boolean saveToken(RememberToken token) {
        String sql = "INSERT INTO remember_tokens (user_id, selector, validator_hash, expires_at) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, token.getUserId());
            stmt.setString(2, token.getSelector());
            stmt.setString(3, token.getValidatorHash());
            stmt.setTimestamp(4, Timestamp.valueOf(token.getExpiresAt()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[RememberTokenDAO] saveToken error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Find a token record by selector.
     * The selector is the public half of the split-token — safe to use for lookup.
     *
     * @param selector the selector string from the cookie
     * @return RememberToken if found, null otherwise
     */
    public RememberToken findBySelector(String selector) {
        String sql = "SELECT * FROM remember_tokens WHERE selector = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, selector);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("[RememberTokenDAO] findBySelector error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Delete a token by selector (called on logout or token rotation).
     *
     * @param selector the selector to delete
     * @return true if deleted
     */
    public boolean deleteBySelector(String selector) {
        String sql = "DELETE FROM remember_tokens WHERE selector = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, selector);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[RememberTokenDAO] deleteBySelector error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete ALL tokens belonging to a user (full logout from all devices).
     *
     * @param userId the user whose tokens to remove
     */
    public void deleteAllForUser(int userId) {
        String sql = "DELETE FROM remember_tokens WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("[RememberTokenDAO] deleteAllForUser error: " + e.getMessage());
        }
    }

    /**
     * Remove all expired tokens (housekeeping — called opportunistically).
     */
    public void deleteExpiredTokens() {
        String sql = "DELETE FROM remember_tokens WHERE expires_at < NOW()";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int deleted = stmt.executeUpdate();
            if (deleted > 0) {
                System.out.println("[RememberTokenDAO] Cleaned up " + deleted + " expired token(s).");
            }

        } catch (SQLException e) {
            System.err.println("[RememberTokenDAO] deleteExpiredTokens error: " + e.getMessage());
        }
    }

    // ─── Private helper ───────────────────────────────────────────────────────

    private RememberToken mapRow(ResultSet rs) throws SQLException {
        RememberToken token = new RememberToken();
        token.setTokenId(rs.getInt("token_id"));
        token.setUserId(rs.getInt("user_id"));
        token.setSelector(rs.getString("selector"));
        token.setValidatorHash(rs.getString("validator_hash"));

        Timestamp expiresAt = rs.getTimestamp("expires_at");
        if (expiresAt != null) {
            token.setExpiresAt(expiresAt.toLocalDateTime());
        }
        token.setCreatedAt(rs.getTimestamp("created_at"));
        return token;
    }
}

