/*
* Copyright 2011-2012 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package upload

import org.vertx.groovy.core.http.HttpClient
import org.vertx.groovy.core.streams.Pump

req = vertx.createHttpClient(port: 8080).put("/someurl") { resp -> println "Response ${resp.statusCode}" }
filename = "upload/upload.txt"
fs = vertx.fileSystem()
fs.props(filename) { ares ->
  def props = ares.result
  println "props is ${props}"
  def size = props.size
  req.headers["Content-Length"] = size
  fs.open(filename) { ares2 ->
    def file = ares2.result
    def rs = file.readStream
    def pump = new Pump(rs, req)
    rs.endHandler { req.end() }
    pump.start()
  }
}


