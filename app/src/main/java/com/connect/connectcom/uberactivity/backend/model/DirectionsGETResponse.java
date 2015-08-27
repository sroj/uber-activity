package com.connect.connectcom.uberactivity.backend.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.List;

/**
 * Data model for the response of a GET request to the Google Directions API.
 * <p/>
 * NOTICE: For now, the class models only the fields required to draw a route using Polylines on a Google Map.
 * <p/>
 * Created by Simon on 8/27/2015.
 */
public class DirectionsGETResponse {

    private Bounds bounds;
    private String copyrights;
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    @Override
    public String toString() {
        return "DirectionsGETResponse{" +
                "bounds=" + bounds +
                ", copyrights='" + copyrights + '\'' +
                ", routes=" + routes +
                '}';
    }

    public static class Route {
        private String summary;
        private List<Leg> legs;

        public List<Leg> getLegs() {
            return legs;
        }

        @Override
        public String toString() {
            return "Route{" +
                    "summary='" + summary + '\'' +
                    ", legs=" + legs +
                    '}';
        }

        public static class Leg {
            private List<Step> steps;

            public List<Step> getSteps() {
                return steps;
            }

            @Override
            public String toString() {
                return "Leg{" +
                        "steps=" + steps +
                        '}';
            }

            public static class Step {

                private Polyline polyline;

                public Polyline getPolyline() {
                    return polyline;
                }

                @Override
                public String toString() {
                    return "Step{" +
                            "polyline=" + polyline +
                            '}';
                }

                public static class Polyline {
                    private String points;
                    private List<LatLng> decodedPoints;

                    public List<LatLng> getDecodedPoints() {
                        if (decodedPoints == null) {
                            decodedPoints = PolyUtil.decode(points);
                        }
                        return decodedPoints;
                    }

                    @Override
                    public String toString() {
                        return "Polyline{" +
                                "points='" + points + '\'' +
                                ", decodedPoints=" + decodedPoints +
                                '}';
                    }
                }
            }
        }
    }

    public static class Bounds {

        private Coordinates northeast;
        private Coordinates southwest;

        public static class Coordinates {
            private Double lat;
            private Double lng;

            public Double getLat() {
                return lat;
            }

            public Double getLng() {
                return lng;
            }

            @Override
            public String toString() {
                return "Coordinates{" +
                        "lat=" + lat +
                        ", lng=" + lng +
                        '}';
            }
        }
    }
}
