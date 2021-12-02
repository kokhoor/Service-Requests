/*
 * Copyright 2021 SolutionX Software Sdn Bhd &lt;info@solutionx.com.my&gt;.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package my.com.solutionx.simplyscript_service.requests;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author SolutionX Software Sdn Bhd &lt;info@solutionx.com.my&gt;
 */
public class RequestsTest {
    @Test
    public void testPost() throws Exception {
        Map<String, Object> mapArgs = new HashMap<>();
        Map<String, String> mapHeaders = new HashMap<>();
        // Map<String, String> mapData = new HashMap<>();
        //mapHeaders.put("Date", "Thu, 02 Dec 2021 10:21:03 GMT");
        //mapHeaders.put("Host", "dm.ap-southeast-1.aliyuncs.com");
        
        // mapArgs.put("url", "https://dm.ap-southeast-1.aliyuncs.com/");
        //mapArgs.put("data", mapData);
        mapArgs.put("url", "https://my.ottimall.com/api/Member/login");
        mapArgs.put("data", "{}");
        mapArgs.put("headers", mapHeaders);
        Requests.post(mapArgs);
    }
}
