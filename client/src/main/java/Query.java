import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class Query {
  private Client client;
  
  private WebTarget webTarget;
  
  public Query(String url) {
    client = ClientBuilder.newClient();
    webTarget = client.target(url);
  }
  
//  public <T> T postText(Object requestEntity, Class<T> responseType) throws
//      ClientErrorException {
//    return
//        webTarget.request(javax.ws.rs.core.MediaType.TEXT_PLAIN)
//            .post(javax.ws.rs.client.Entity.entity(requestEntity,
//                javax.ws.rs.core.MediaType.TEXT_PLAIN),
//                responseType);
//  }
  
  public int getSteps() throws ClientErrorException {
    WebTarget resource = webTarget;
    return
        resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(Integer.class);
  }
  
}
