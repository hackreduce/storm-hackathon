namespace java com.hopper.geocoder

enum AddressComponentType {
  country                       = 1;
  administrative_area_level_1   = 2;
  administrative_area_level_2   = 3;
  administrative_area_level_3   = 4;
  locality                      = 5;
  sub_locality                  = 6;
  neighborhood                  = 7;
  postal_code                   = 8;
  intersection                  = 9;
  route                         = 10;
  street_address                = 11;
}

struct AddressComponent {
    1: required string short_name;
    2: required string full_name;
    3: required AddressComponentType type;
}

struct LatLng {
  1: required double lat;
  2: required double lng;
}

struct Geometry {
  1: required LatLng location
}

struct Address {
    1: required list<AddressComponent> parts;
    2: required Geometry geometry;
    3: required string formattedAddress = "";
}

struct Geo {
  1: list<Address> addresses  
}

enum GeocoderError {
  # When the request exceeds the rate limit
  # clients may retry
  unavailable = 1;
  # Unexpected failure
  unknown_failure = 2;
}

exception GeocoderException {
  1: GeocoderError error_code,
  2: optional string error_msg
}

service Geocoder {
  Geo geocode(1: string address) throws (1:GeocoderException ex)
  Geo reverseGeocode(1: double lat, 2: double lng) throws (1:GeocoderException ex)
}