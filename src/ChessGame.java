import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChessGame {

    public enum ValidationStatus {
        PENDING,
        SYNTAX_OK,
        VALID,
        SYNTAX_ERROR,
        LOGIC_ERROR
    }

    private final Map<String, String> tags;
    private final List<String> pgnMoveStrings;
    private final List<String> syntaxErrors;
    private String logicalError;
    private ValidationStatus status;
    private int errorMoveIndex = -1;


    public ChessGame() {
        this.tags = new HashMap<>();
        this.pgnMoveStrings = new ArrayList<>();
        this.syntaxErrors = new ArrayList<>();
        this.logicalError = null;
        this.status = ValidationStatus.PENDING;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public List<String> getPgnMoveStrings() {
        return pgnMoveStrings;
    }

    public List<String> getSyntaxErrors() {
        return syntaxErrors;
    }

    public String getLogicalError() {
        return logicalError;
    }

    public ValidationStatus getStatus() {
        return status;
    }

    public int getErrorMoveIndex() {
        return errorMoveIndex;
    }


    public void addTag(String key, String value) {
        this.tags.put(key, value);
    }

    public void addMoveString(String move) {
        this.pgnMoveStrings.add(move.trim());
    }

    public void addSyntaxError(String error) {
        this.syntaxErrors.add(error);
        this.status = ValidationStatus.SYNTAX_ERROR;
    }

    public void setLogicalError(String error, int moveIndex) {
        this.logicalError = error;
        this.errorMoveIndex = moveIndex;
        this.status = ValidationStatus.LOGIC_ERROR;
    }

    public void setStatus(ValidationStatus status) {
        this.status = status;
        if (status != ValidationStatus.LOGIC_ERROR) {
            this.logicalError = null;
            this.errorMoveIndex = -1;
        }
    }

    public boolean hasSyntaxErrors() {
        return !syntaxErrors.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Game Status: ").append(status).append("\n");
        sb.append("Tags: ").append(tags).append("\n");
        if (hasSyntaxErrors()) {
            sb.append("Syntax Errors:\n");
            syntaxErrors.forEach(err -> sb.append("  - ").append(err).append("\n"));
        }
        if (logicalError != null) {
            sb.append("Logical Error at move ").append(errorMoveIndex + 1)
                    .append(" ('").append(pgnMoveStrings.get(errorMoveIndex))
                    .append("'): ").append(logicalError).append("\n");
        }
        return sb.toString();
    }
}