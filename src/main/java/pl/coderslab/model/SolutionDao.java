package pl.coderslab.model;

import pl.coderslab.model.DbUtil;
import pl.coderslab.model.Solution;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SolutionDao {

    private static final String CREATE_SOLUTION_QUERY =
            "INSERT INTO solution(created, users_id, exercise_id, description) VALUES (now(), ?, ?, ?)";
    private static final String READ_SOLUTION_QUERY =
            "SELECT * FROM solution where users_id = ?";
    private static final String UPDATE_SOLUTION_QUERY =
            "UPDATE solution SET created = ?, updated = ?, description = ? where id = ?";
    private static final String DELETE_SOLUTION_QUERY =
            "DELETE FROM solution WHERE id = ?";
    private static final String FIND_ALL_SOLUTION_QUERY =
            "SELECT * FROM solution";

    private static final String FIND_RECENT = "SELECT * FROM solution ORDER BY created LIMIT ?";

    public List<Solution> findRecent(int size) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(FIND_RECENT)) {
            int idx = 0;
            statement.setInt(++idx, size);
            ResultSet rs = statement.executeQuery();
            ArrayList<Solution> solutions = new ArrayList<>();
            while (rs.next()) {
                Solution solution = new Solution(
                        rs.getInt("id"),
                        rs.getString("created"),
                        rs.getString("updated"),
                        rs.getString("description"));
                solutions.add(solution);
            }
            return solutions;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("cannot find recent due to: " + e);
        }
    }

    public Solution create(Solution solution) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_SOLUTION_QUERY, Statement.RETURN_GENERATED_KEYS);
            int idx = 0;
            statement.setInt(idx++, solution.getIdUser());
            statement.setInt(idx++, solution.getIdExercise());
            statement.setString(idx++, solution.getDescription());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                solution.setId(resultSet.getInt(1));
            }
            resultSet.close();
            return solution;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Solution read(int solutionId) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(READ_SOLUTION_QUERY);
            statement.setInt(1, solutionId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Solution solution = new Solution(
                        resultSet.getInt("id"),
                        resultSet.getString("created"),
                        resultSet.getString("updated"),
                        resultSet.getString("description")
                );
                return solution;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Solution[] findAllByUserId(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            Solution[] solutions = new Solution[0];
            PreparedStatement statement = conn.prepareStatement(READ_SOLUTION_QUERY);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Solution solution = new Solution(
                        resultSet.getInt("id"),
                        resultSet.getString("created"),
                        resultSet.getString("updated"),
                        resultSet.getString("description")
                );
                solutions = addToArray(solution, solutions);
            }
            return solutions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void update(Solution solution) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_SOLUTION_QUERY);
            statement.setString(1, solution.getCreated());
            statement.setString(2, solution.getUpdated());
            statement.setString(3, solution.getDescription());
            statement.setInt(4, solution.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int solutionId) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_SOLUTION_QUERY);
            statement.setInt(1, solutionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Solution[] findAll() {
        try (Connection conn = DbUtil.getConnection()) {
            Solution[] solutions = new Solution[0];
            PreparedStatement statement = conn.prepareStatement(FIND_ALL_SOLUTION_QUERY);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Solution solution = new Solution(
                        resultSet.getInt("id"),
                        resultSet.getString("created"),
                        resultSet.getString("updated"),
                        resultSet.getString("description"));
                solutions = addToArray(solution, solutions);
            }
            return solutions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Solution[] addToArray(Solution s, Solution[] solutions) {
        Solution[] tmpSolutions = Arrays.copyOf(solutions, solutions.length + 1);
        tmpSolutions[solutions.length] = s;
        return tmpSolutions;
    }


}
