package io.github.mat3e;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;


public class HelloServiceTest {

    private final static String WELCOME = "Hello";
    private final static String FALLBACK_ID_WELCOME = "Hello";


    @Test
    public void test_prepareGreeting_nullName_returnsGreetingWithFallback() {
        var mockRepository = alwaysReturningHelloRepository();
        var SUT = new HelloService(mockRepository);
        var result = SUT.prepareGreeting(null, "-1");
        assertEquals(WELCOME + " " + HelloService.FALLBACK_NAME + "!", result);
    }

    @Test
    public void test_prepareGreeting_name_returnsGreetingWithNameOn()  {
        var mockRepository = alwaysReturningHelloRepository();
        var SUT = new HelloService(mockRepository);
        String name = "test";
        var result = SUT.prepareGreeting(name, "-1");
        assertEquals(WELCOME + " " + name + "!", result);

    }
    @Test
    public void test_prepareGreeting_nullLang_returnGreetingWitFallbackIdLang() {
        var mockRepository = fallbackLangIdRepository();
        var SUT = new HelloService(mockRepository);
        var result = SUT.prepareGreeting(null, null);
        assertEquals(FALLBACK_ID_WELCOME + " " + HelloService.FALLBACK_NAME + "!", result);
    }

    @Test
    public void test_prepareGreeting_textLang_returnGreetingWitFallbackIdLang() {
        var mockRepository = fallbackLangIdRepository();
        var SUT = new HelloService(mockRepository);
        var result = SUT.prepareGreeting(null, "abc");
        assertEquals(FALLBACK_ID_WELCOME + " " + HelloService.FALLBACK_NAME + "!", result);
    }

    @Test
    public void test_prepareGreeting_nonExistingLang_returnsGreetingWithFallbackLang(){
        var mockRepository = new LangRepository(){
        @Override
                Optional<Lang> findById(Long id){
                return Optional.empty();
            }
            };

        var SUT = new HelloService(mockRepository);
        var result = SUT.prepareGreeting(null, "-1");
        assertEquals(HelloService.FALLBACK_LANG.getWelcomeMsg() + " " + HelloService.FALLBACK_NAME + "!", result);
    }
    

    private LangRepository fallbackLangIdRepository() {
        return new LangRepository() {
            @Override
            Optional<Lang> findById(Long id) {
                if ((id.equals(HelloService.FALLBACK_LANG.getId()))){
                    return Optional.of((new Lang(null, FALLBACK_ID_WELCOME, null)));
                }
                return Optional.empty();
            }
        };
    }

    private LangRepository alwaysReturningHelloRepository() {
        return new LangRepository() {
            @Override
            Optional<Lang> findById(Long id) {
                return Optional.of(new Lang(null, WELCOME, "null"));
            }
        };
    }
}