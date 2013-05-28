public class GPSUtils
{

    public static final double BASE_LAT = 52.4;
    public static final double BASE_LON = -4.1;

    /**
    calculates a position given the current position, a bearing (in degrees) and a distance (in km)
    @param bearing - the bearing from the current position in degrees
    @param lat1 - the latitude in degrees
    @param lon1 - the longitude in degrees 
    @param distance - the distance to project in km
    @return a 2 element array with the projected lat and long 
    */
    public static  double[] projectPoint(double bearing,double dist,double lat1,double lon1)
    {
        double lat2,lon2;
        //dist = dist/6367;  // d = angular distance covered on earth's surface
        double result[] = new double[2];

                

        //convert to radians
        bearing = Math.toRadians(bearing);
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);

        lat2 = Math.asin( Math.sin(lat1)*Math.cos(dist/6367) + Math.cos(lat1)*Math.sin(dist/6367)*Math.cos(bearing) );
        lon2 = lon1 + Math.atan2(Math.sin(bearing)*Math.sin(dist/6367)*Math.cos(lat1), Math.cos(dist/6367)-Math.sin(lat1)*Math.sin(lat2));

        result[0]=Math.toDegrees(lat2);
        result[1]=Math.toDegrees(lon2);

        return result;
    }

    /**
    converts x/y co-ordinates to a WGS84 latitude and longitude
    @param x - the x co-ordinate in metres 
    @param y - the y co-ordinate in metres
    @return a 2 element array with the latitude and longitude
    */
    public static double[] xYToLatLon(double x,double y)
    {
        double org_azimuth;

        //figure out the angle from the origin to our current location 

        //handle potential divide by 0
        if(Math.abs(x)>0.00000000001)
        {
            org_azimuth=Math.atan(Math.abs(y)/Math.abs(x));
        }
        else
        {
            org_azimuth=Math.atan(0.0);
        }

        org_azimuth=org_azimuth*(180/Math.PI);

        if(org_azimuth<0)
        {
            org_azimuth=org_azimuth+360;
        }
        //current point is above and right of last one
        if(x>0.0&&y>0.0)
        {
            org_azimuth=90-org_azimuth;
        }
        
        //current point is above and left of the last one
        else if(x<0.0&&y>0.0)
        {
            org_azimuth=270+org_azimuth;
        }
        
        //below and right
        else if(x>0.0&&y<0.0)
        {
            org_azimuth=90+org_azimuth;
        }
        
        //below and left
        else if(x<0.0&&y<0.0)
        {
            org_azimuth=270-org_azimuth;
        }
    

        //distance from the origin in metres
        double org_dist=Math.abs(Math.sqrt(Math.pow(x,2)+Math.pow(y,2)));

        //project a point to figure out our lat/lon
        double [] gps_position=GPSUtils.projectPoint(org_azimuth,org_dist/1000.0,BASE_LAT,BASE_LON);

        return gps_position;
    }

/*    public static void main(String args[])
    {
        double pos[] = xYToLatLon(1000,0);
        System.out.println("Lat: " + pos[0] + " Lon: " + pos[1]);
    }*/

}