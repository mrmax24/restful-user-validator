package app.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataWrapper<T> {
    private T data;
    private Map<String, Object> metadata;

    public DataWrapper(T data) {
        this.data = data;
        this.metadata = new HashMap<>();
    }

    public static <T> DataWrapper<T> empty() {
        return new DataWrapper<>(null);
    }

    public void addMetadata(String key, Object value) {
        this.metadata.put(key, value);
    }
}
