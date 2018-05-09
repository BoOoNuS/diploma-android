package ua.nure.heating_system_mobile.requestor;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ua.nure.heating_system_mobile.model.Heating;
import ua.nure.heating_system_mobile.model.HeatingConfiguration;
import ua.nure.heating_system_mobile.model.User;

/**
 * @author Stanislav_Vorozhka
 */
@Slf4j
public class GatewayRequestor {

    private static final HttpClient CLIENT = new HttpClient();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String URI = "http://localhost:8004";

    public User getUserByLoginAndPassword(String login, String password) {
        GetMethod getMethod = new GetMethod(URI + "/user/authenticate");
        HttpMethodParams params = new HttpMethodParams();
        params.setParameter("login", login);
        params.setParameter("password", password);
        getMethod.setParams(params);
        User user = null;
        try {
            CLIENT.executeMethod(getMethod);
            user = OBJECT_MAPPER.readValue(getMethod.getResponseBodyAsString(), User.class);
        } catch (IOException e) {
            log.warn("GatewayRequestor#getUserByLoginAndPassword cannot connect to server", e);
        }

        return user;
    }

    public void reConfigHeating(long userId, long heatingId, HeatingConfiguration heatingConfiguration) throws IOException {
        PutMethod putMethod = new PutMethod(MessageFormat.format("{0}/heating/user/{1}/heating/{2}", URI, userId, heatingId));
        putMethod.setRequestEntity(new StringRequestEntity(
                OBJECT_MAPPER.writeValueAsString(heatingConfiguration),
                "application/json",
                "UTF-8"));
        CLIENT.executeMethod(putMethod);
    }

    public List<Heating> getUserHeatings(long userId) {
        GetMethod getMethod = new GetMethod(MessageFormat.format("{0}/related/{1}", URI, userId));
        List<Heating> heatings;
        try {
            CLIENT.executeMethod(getMethod);
            heatings = OBJECT_MAPPER.readValue(getMethod.getResponseBodyAsString(), OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, Heating.class));
        } catch (IOException e) {
            log.warn("GatewayRequestor#getUserHeatings cannot connect to server", e);
            heatings = new ArrayList<>();
        }

        return heatings;
    }
}
