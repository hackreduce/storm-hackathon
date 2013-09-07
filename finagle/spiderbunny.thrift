namespace java com.hopper.spiderbunny.thrift

struct HttpResponse {
  1: required i32           status_code;
  2: map<string, string>    headers;
  3: optional binary        data;
}

enum SpiderBunnyErrorCode {
  INVALID_URL = 0;
  ROBOTS_DISALLOW = 1;
  RATE_LIMITED = 2;
  TIMED_OUT = 3;
  CONNECTION_ERROR = 4;
}

exception SpiderBunnyException {
  1: required SpiderBunnyErrorCode  errorCode;
  2: required string                message;
}

service SpiderBunny {
  HttpResponse fetch(1:string url) throws(1: SpiderBunnyException e)
}
