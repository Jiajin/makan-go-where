package com.makan.makangowhere;

import com.makan.makangowhere.models.AcceptInviteRequest;
import com.makan.makangowhere.models.CreateMeetingRequest;
import com.makan.makangowhere.models.CreatePersonRequest;
import com.makan.makangowhere.models.CreatePlaceRequest;
import com.makan.makangowhere.models.FinalizeMeetingRequest;
import com.makan.makangowhere.models.GetMeetingRequest;
import com.makan.makangowhere.models.GetPersonRequest;
import com.makan.makangowhere.models.Meeting;
import com.makan.makangowhere.models.Person;
import com.makan.makangowhere.models.Place;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MakanGoWhereApplicationTests {

    // @Test
    // void contextLoads() {
    // }

    @Autowired
    protected TestRestTemplate testRestTemplate;

    private final String savePersonUrl = "/api/v1/client/savePerson";
    private final String saveMeetingUrl = "/api/v1/client/saveMeeting";
    private final String finalizeMeetingUrl = "/api/v1/client/finalizeMeeting";
    private final String createPlaceUrl = "/api/v1/client/createPlace";
    private final String acceptInviteUrl = "/api/v1/client/acceptInvite";
    private final String getPersonUrl = "/api/v1/client/getPerson";
    private final String getMeetingByIdUrl = "/api/v1/client/getMeetingById";

    @Test
    public void e2eIntegrationTest() {

        // 1. Create Initial Host and Meeting
        CreatePersonRequest personRequest = new CreatePersonRequest("Host", "123@gmail.com");
        ResponseEntity<Person> response1 = sendRequest(savePersonUrl, personRequest, Person.class);

        assertEquals(HttpStatus.CREATED, response1.getStatusCode());
        Person host = (Person) response1.getBody();
        assertNotNull(host);

        CreateMeetingRequest meetingReq = new CreateMeetingRequest("meetingName", host.getId());
        ResponseEntity<Meeting> response2 = sendRequest(saveMeetingUrl, meetingReq, Meeting.class);
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());
        Meeting meeting = (Meeting) response2.getBody();
        assertNotNull(meeting);

        // 2. Invite people and create records for them
        AcceptInviteRequest invitee1Req = new AcceptInviteRequest("Friend1", "456@gmail.com", meeting.getId());
        ResponseEntity<Person> response3 = sendRequest(acceptInviteUrl, invitee1Req, Person.class);
        assertEquals(HttpStatus.OK, response3.getStatusCode());
        Person invitee1 = (Person) response3.getBody();
        assertNotNull(invitee1);

        AcceptInviteRequest invitee2Req = new AcceptInviteRequest("Friend2", "789@gmail.com", meeting.getId());
        ResponseEntity<Person> response4 = sendRequest(acceptInviteUrl, invitee2Req, Person.class);
        assertEquals(HttpStatus.OK, response4.getStatusCode());
        Person invitee2 = (Person) response4.getBody();
        assertNotNull(invitee2);

        // 3. Invitee login to retrieve meetings and create Locations
        // 3a Invitee1
        GetPersonRequest login1Req = new GetPersonRequest(invitee1.getEmail());
        ResponseEntity<Person> response5 = sendRequest(getPersonUrl, login1Req, Person.class);
        assertEquals(HttpStatus.OK, response5.getStatusCode());
        Person login1 = (Person) response5.getBody();
        assertNotNull(login1);
        assertEquals(meeting.getId(), login1.getMeetingCsvList().get(0));

        CreatePlaceRequest invitee1PlaceReq = new CreatePlaceRequest("Mcd", "address", login1.getId(), meeting.getId());
        ResponseEntity<Place> response6 = sendRequest(createPlaceUrl, invitee1PlaceReq, Place.class);
        assertEquals(HttpStatus.CREATED, response6.getStatusCode());
        Place place1 = (Place) response6.getBody();
        assertNotNull(place1);

        // 3b Invitee2
        GetPersonRequest login2Req = new GetPersonRequest(invitee2.getEmail());
        ResponseEntity<Person> response7 = sendRequest(getPersonUrl, login2Req, Person.class);
        assertEquals(HttpStatus.OK, response7.getStatusCode());
        Person login2 = (Person) response7.getBody();
        assertNotNull(login2);
        assertEquals(meeting.getId(), login2.getMeetingCsvList().get(0));

        CreatePlaceRequest invitee2PlaceReq = new CreatePlaceRequest("BK", "address", login2.getId(), meeting.getId());
        ResponseEntity<Place> response8 = sendRequest(createPlaceUrl, invitee2PlaceReq, Place.class);
        assertEquals(HttpStatus.CREATED, response8.getStatusCode());
        Place place2 = (Place) response8.getBody();
        assertNotNull(place2);

        // 3c Host
        GetPersonRequest login3Req = new GetPersonRequest(host.getEmail());
        ResponseEntity<Person> response9 = sendRequest(getPersonUrl, login3Req, Person.class);
        assertEquals(HttpStatus.OK, response9.getStatusCode());
        Person hostLogin = (Person) response9.getBody();
        assertNotNull(hostLogin);
        assertEquals(meeting.getId(), hostLogin.getMeetingCsvList().get(0));

        CreatePlaceRequest hostPlaceReq = new CreatePlaceRequest("BK", "address", login2.getId(), meeting.getId());
        ResponseEntity<Place> response10 = sendRequest(createPlaceUrl, hostPlaceReq, Place.class);
        assertEquals(HttpStatus.CREATED, response10.getStatusCode());
        Place place3 = (Place) response10.getBody();
        assertNotNull(place3);

        // 3d Create new user that cannot add Location to Meeting
        CreatePersonRequest outsiderReq = new CreatePersonRequest("Host", "Abc@gmail.com");
        ResponseEntity<Person> outsiderResp = sendRequest(savePersonUrl, outsiderReq, Person.class);
        assertEquals(HttpStatus.CREATED, outsiderResp.getStatusCode());
        Person outsider = (Person) outsiderResp.getBody();
        assertNotNull(outsider);

        CreatePlaceRequest outsiderPlaceReq = new CreatePlaceRequest("Nowhere", "address", outsider.getId(),
                meeting.getId());
        ResponseEntity<Place> outsiderErrResp = sendRequest(createPlaceUrl, outsiderPlaceReq, Place.class);
        assertEquals(HttpStatus.NOT_FOUND, outsiderErrResp.getStatusCode());

        // 4. Host Finalize meeting
        // 4a. Invitee unable to finalize meeting
        FinalizeMeetingRequest InviteeFinalReq = new FinalizeMeetingRequest(meeting.getId(), login2.getId());
        ResponseEntity<Meeting> expErr = sendRequest(finalizeMeetingUrl, InviteeFinalReq, Meeting.class);
        assertEquals(HttpStatus.NOT_FOUND, expErr.getStatusCode());

        // 4b. Host finalize and retrieve after
        FinalizeMeetingRequest finalReq = new FinalizeMeetingRequest(meeting.getId(), hostLogin.getId());
        ResponseEntity<Meeting> resp11 = sendRequest(finalizeMeetingUrl, finalReq, Meeting.class);
        assertEquals(HttpStatus.OK, resp11.getStatusCode());
        Meeting finalMeeting = (Meeting) resp11.getBody();
        assertNotNull(finalMeeting);

        GetMeetingRequest getMeetingReq = new GetMeetingRequest(meeting.getId());
        ResponseEntity<Meeting> resp12 = sendRequest(getMeetingByIdUrl, getMeetingReq, Meeting.class);
        assertEquals(HttpStatus.OK, resp12.getStatusCode());
        Meeting retrievedMeeting = (Meeting) resp12.getBody();
        assertNotNull(retrievedMeeting);
        assertEquals(1, retrievedMeeting.getPlaces().stream().filter(x -> x.getChosen() == true).count());
        assertEquals("FINAL", retrievedMeeting.getStatus().toString());

        // 5. Unable to add new location or re-finalize meeting
        CreatePlaceRequest newPlaceReq = new CreatePlaceRequest("KFC", "address", login2.getId(), meeting.getId());
        ResponseEntity<Place> expErr2 = sendRequest(createPlaceUrl, newPlaceReq, Place.class);
        assertEquals(HttpStatus.NOT_FOUND, expErr2.getStatusCode());

        FinalizeMeetingRequest failFinalReq = new FinalizeMeetingRequest(meeting.getId(), hostLogin.getId());
        ResponseEntity<Meeting> expErr3 = sendRequest(finalizeMeetingUrl, failFinalReq, Meeting.class);
        assertEquals(HttpStatus.NOT_FOUND, expErr3.getStatusCode());

    }

    private <T> ResponseEntity<T> sendRequest(String url, Object requestBody, Class<T> responseType) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(requestBody);
        return testRestTemplate.exchange(url, HttpMethod.POST, httpEntity, responseType);
    }
}
