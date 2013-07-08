package com.maxmind.geoip2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.Omni;
import com.maxmind.geoip2.record.LocationRecord;
import com.maxmind.geoip2.record.MaxMindRecord;
import com.maxmind.geoip2.record.PostalRecord;
import com.maxmind.geoip2.record.SubdivisionRecord;
import com.maxmind.geoip2.record.TraitsRecord;

public class OmniTest {
    private Omni omni;

    @Before
    public void createClient() throws IOException, GeoIp2Exception {
        HttpTransport transport = new TestTransport();

        WebServiceClient client = new WebServiceClient.Builder(42, "012345689")
                .transport(transport).build();

        this.omni = client.omni(InetAddress.getByName("1.1.1.1"));
    }

    @Test
    public void testSubdivisionsList() {
        List<SubdivisionRecord> subdivisionsList = this.omni.getSubdivisionsList();
        assertNotNull("city.getSubdivisionsList returns null", subdivisionsList);
        if (subdivisionsList.size() == 0) {
            fail("subdivisionsList is empty");
        }
        SubdivisionRecord subdivision = subdivisionsList.get(0);
        assertEquals("subdivision.getConfidence() does not return 88",
                new Integer(88), subdivision.getConfidence());
        assertEquals("subdivision.getGeoNameId() does not return 574635",
                574635, subdivision.getGeoNameId().intValue());
        assertEquals("subdivision.getCode() does not return MN", "MN",
                subdivision.getIsoCode());
    }

    @Test
    public void mostSpecificSubdivision() {
        assertEquals("Most specific subdivision returns last subdivision",
                "TT", this.omni.getMostSpecificSubdivision().getIsoCode());
    }

    @SuppressWarnings("boxing")
    @Test
    public void testTraits() {
        TraitsRecord traits = this.omni.getTraits();

        assertNotNull("city.getTraits() returns null", traits);
        assertEquals("traits.getAutonomousSystemNumber() does not return 1234",
                new Integer(1234), traits.getAutonomousSystemNumber());
        assertEquals(
                "traits.getAutonomousSystemOrganization() does not return AS Organization",
                "AS Organization", traits.getAutonomousSystemOrganization());
        assertEquals(
                "traits.getAutonomousSystemOrganization() does not return example.com",
                "example.com", traits.getDomain());
        assertEquals("traits.getIpAddress() does not return 1.2.3.4",
                "1.2.3.4", traits.getIpAddress());
        assertEquals("traits.isAnonymousProxy() returns true", true,
                traits.isAnonymousProxy());
        assertEquals("traits.isSatelliteProvider() returns true", true,
                traits.isSatelliteProvider());
        assertEquals("traits.getIsp() does not return Comcast", "Comcast",
                traits.getIsp());
        assertEquals("traits.getOrganization() does not return Blorg", "Blorg",
                traits.getOrganization());
        assertEquals("traits.getUserType() does not return userType",
                "college", traits.getUserType());
    }

    @Test
    public void testLocation() {

        LocationRecord location = this.omni.getLocation();

        assertNotNull("city.getLocation() returns null", location);

        assertEquals("location.getAccuracyRadius() does not return 1500",
                new Integer(1500), location.getAccuracyRadius());

        double latitude = location.getLatitude().doubleValue();
        assertEquals("location.getLatitude() does not return 44.98", 44.98,
                latitude, 0.1);
        double longitude = location.getLongitude().doubleValue();
        assertEquals("location.getLongitude() does not return 93.2636",
                93.2636, longitude, 0.1);
        assertEquals("location.getMetroCode() does not return 765",
                new Integer(765), location.getMetroCode());
        assertEquals("location.getTimeZone() does not return America/Chicago",
                "America/Chicago", location.getTimeZone());
    }

    @Test
    public void testMaxMind() {
        MaxMindRecord maxmind = this.omni.getMaxMind();
        assertEquals("Correct number of queries remaining", 11, maxmind
                .getQueriesRemaining().intValue());
    }

    @Test
    public void testPostal() {

        PostalRecord postal = this.omni.getPostal();
        assertEquals("postal.getCode() does not return 55401", "55401",
                postal.getCode());
        assertEquals("postal.getConfidence() does not return 33", new Integer(
                33), postal.getConfidence());

    }

    @Test
    public void testRepresentedCountry() {
        assertNotNull("city.getRepresentedCountry() returns null",
                this.omni.getRepresentedCountry());

        assertEquals(
                "city.getRepresentedCountry().getType() does not return C<military>",
                "C<military>", this.omni.getRepresentedCountry().getType());
    }
}