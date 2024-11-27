package rs._1._4.rs1124.service.process.checkout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import rs._1._4.rs1124.service.process.checkout.CheckoutProcessTest;
import rs._1._4.rs1124.service.process.checkout.CheckoutTestCase;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Stream;

import static rs._1._4.rs1124.presentation.Reference.ERROR_FILE_LOAD_JSON;

public class CheckoutTestCaseArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        Type listType = new TypeToken<List<CheckoutTestCase>>() {}.getType();
        try (InputStream inputStream = CheckoutProcessTest.class.getResourceAsStream("/test-cases.json")) {
            Gson gson = new Gson();
            return ((List<CheckoutTestCase>)gson.fromJson(new InputStreamReader(inputStream), listType))
                    .stream().map(Arguments::of);
        } catch (IOException e) {
            throw new RuntimeException(ERROR_FILE_LOAD_JSON, e);
        }
    }
}
