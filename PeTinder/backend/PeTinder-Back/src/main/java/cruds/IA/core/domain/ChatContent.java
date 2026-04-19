package cruds.IA.core.domain;

public class ChatContent {

    private String type; // text | image
    private String value;

    public ChatContent(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
