namespace java com.hopper.hcache

exception HCacheException {
 1: i32 error_code,
 2: string error_msg
}

service HCache {
  void put(1:string namespace, 2:string key, 3:binary value) throws(1:HCacheException e),
  binary get(1:string namespace, 2:string key, 3:i64 max_age) throws(1:HCacheException e)
}
