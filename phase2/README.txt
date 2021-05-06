Phase2 | Group number: group_0112
========================================================================================================================
Please consider reading the Phase 1 README if you need any clarifications on how to start and run the system or our Phase
1 implementation.
========================================================================================================================

Here we will focus on our Phase 2 implementation and instructions.

Mandatory Extensions and How They Work:
    1) We have three types of events: party, talk, and panel discussion. All events start as party's, if you add one
    speaker it becomes a talk, and if you add numerous (2+) speakers, it becomes a panel discussion. You cannot
    un-schedule a speaker hence, once an event becomes a panel, it cannot revert to being a talk or party, and similarly
    a talk cannot become a party. Our design allows for easy extension to change the type of event by un-scheduling
    speakers, however, we decided not to implement that as it was not required.

    2) Each event has to start on the hour, but can last as many HOURS as the organizer wants provided it's within the
    9-5 range.

    3) Events can be cancelled by any organizer. Once signed in as an organizer, select option 4,
    "Create/Delete/Modify Event" and select the "Delete" option. Then chose which event you want to delete.

    4) Now we have the admin user who can delete single messages between any two users, or delete a selected event with
    no attendees.

    5) Organizers can also create Speaker accounts, Attendee accounts, and Organizer accounts. We have chosen not to
    allow organizers to create admin accounts. Although this is easily implementable, we thought it was a security flaw.

    6) When an organizer creates an event, they can set their desired capacity for the event, provided its less than
    500. This capacity can be changed later. Whenever an attendee wants to join an event, they are presented with a list
    of events they can join. If an event has reached it's max capacity, then it won't show up on this list, and
    therefore will prevent the event from going over its capacity.


Optional Extensions (Approved by Prof)
    1) Whenever the system is started, all the events that have already occurred (are in the past) are deleted from the
    system. This prevents anyone from enrolling for that event. Also, this event will be removed from its participants'
    list of scheduled events, from the speakers' list of assigned events and its organizer's list of created events.

    2) The system shows a list of items when the user wants to do certain tasks. This is like a dropdown menu. The
    reason for this was to provide the user with a visual list of options rather than having them manually enter
    userids/eventids/speakerids which make's it inconvenient for the user. A list of contacts is presented when the user
    wants to add/remove a contact or send a message to one. A list of events is displayed for the user to chose when
    they want to signup or leave one. A list of speakers is shown when an organizer wants to schedule one. It's in
    numerous other places in the program.

    3) The system allows additional user requests, like dietary restrictions, accessibility requirements etc. Attendees
    can make requests and view the status of their request(s). The organizers can change the status of these request(s)
    to either "Pending" or "Addressed".

    4) When an organizer creates an event, they don't need to worry about choosing the room. This is done automatically
    by our program. The event will only be created if there is a room that exists that has space at the desired time and
    for the given length.

    5) Throughout our program, the user can always decide to "go back" to the previous menu without completing the task
    at hand. This is handy in case they accidentally click a button or decide they want to change a previous input.

    6) Organizer can export a .txt file with all the events and their info, so they can eventually print it out and
    have it on hand during the conference to keep a track of when is which event, and who's speaking, and who's
    attending. The file exported is called : "eventinfo.txt"

    7) (Small Extension) Any organizer can change the name of any event.


IF YOU FACE ANY UNEXPECTED ERRORS AND REQUIRE CLARIFICATION, PLEASE EMAIL US.