package frameworks.retrofit;


import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.http.PartMap;
public class RetrofitConverter extends Converter.Factory {

    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (type == String.class) {
            return StringResponseConverter.INSTANCE;
        }
        return null;
    }

    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations, Retrofit retrofit) {
        if (type == String.class && parameterAnnotations.length > 0 &&
                parameterAnnotations[0].annotationType() == PartMap.class)
            return StringRequestConverterMultipart.INSTANCE;
        else if (type == String.class)
            return StringRequestConverter.INSTANCE;
        else if (type == File.class)
            return FileRequestConverter.INSTANCE;

        return null;
    }


    private static class FileRequestConverter implements Converter<File, RequestBody> {
        public static final FileRequestConverter INSTANCE = new FileRequestConverter();
        private static final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("multipart/form-data");

        @Override
        public RequestBody convert(File file) throws IOException {
            Log.v("", "FileRequestConverter " + file);
            return RequestBody.create(MEDIA_TYPE_IMAGE, file);
        }
    }
    private static class StringRequestConverter implements Converter<String, RequestBody> {


        public static final StringRequestConverter INSTANCE = new StringRequestConverter();
        private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json");

        @Override
        public RequestBody convert(String value) throws IOException {
            Log.v("", "StringRequestConverter " + value);
            RequestBody fileBody = null;
            try {
                fileBody = RequestBody.create(MEDIA_TYPE_JSON, value.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return fileBody;
        }
    }

    private static class StringRequestConverterMultipart implements Converter<String, RequestBody> {

        public static final StringRequestConverterMultipart INSTANCE = new StringRequestConverterMultipart();
        private static final MediaType MEDIA_TYPE_MULTIPART = MediaType.parse("multipart/form-data");

        @Override
        public RequestBody convert(String value) throws IOException {
            Log.v("", "StringRequestConverter " + value);
            RequestBody fileBody = null;
            try {
                fileBody = RequestBody.create(MEDIA_TYPE_MULTIPART, value.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return fileBody;
        }
    }

    private static class StringResponseConverter implements Converter<ResponseBody, String> {
        public static final StringResponseConverter INSTANCE = new StringResponseConverter();

        @Override
        public String convert(ResponseBody value) throws IOException {
            String output = new String(value.bytes());
            Log.v("", "StringResponseConverter " + output);
            return output;
        }
    }
}