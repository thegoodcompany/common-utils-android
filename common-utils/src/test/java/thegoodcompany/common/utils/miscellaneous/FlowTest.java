package thegoodcompany.common.utils.miscellaneous;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static thegoodcompany.common.utils.GeneralUnitTest.printDiff;
import static thegoodcompany.common.utils.GeneralUnitTest.printDuration;
import static thegoodcompany.common.utils.GeneralUnitTest.printTestInfo;
import static thegoodcompany.common.utils.miscellaneous.Flow.flow;

public class FlowTest {
    private static final String UID1 = "dlcroyiq";
    private static final String UID2 = "crobetud";

    private static final String U_1_PERSONAL_EMAIL = "someone1@examiple.com";
    private static final String U_2_WORK_EMAIL = "someone2@contoso.com";
    private static final String U_2_SCHOOL_EMAIL = "someone2@school.com";

    private final String[] mEmailStore = new String[1];

    @Test
    public void flow_isCorrect() {
        Long a = 5L;
        Long b = null;

        int aInt = flow(a, Long::intValue, 1);
        int bInt = flow(b, Long::intValue, 1);

        assertThat(aInt).isEqualTo(5);
        assertThat(bInt).isEqualTo(1);

        String notFound = "Not found";

        Map<String, User> users = getUsers();
        String u1PersonalEmail = Flow.start(users.get(UID1))
                .then(User::getEmails)
                .then(mapping -> mapping.get(User.EmailField.PERSONAL))
                .then(Email::getAddress)
                .getValue(notFound);
        String u1WorkEmail = Flow.start(users.get(UID1))
                .then(User::getEmails)
                .then(mapping -> mapping.get(User.EmailField.WORK))
                .then(Email::getAddress)
                .getValue(notFound);
        String u1SchoolEmail = Flow.start(users.get(UID1))
                .then(User::getEmails)
                .then(mapping -> mapping.get(User.EmailField.SCHOOL))
                .then(Email::getAddress)
                .getValue(notFound);

        String u2PersonalEmail = Flow.start(users.get(UID2))
                .then(User::getEmails)
                .then(mapping -> mapping.get(User.EmailField.PERSONAL))
                .then(Email::getAddress)
                .getValue();
        String u2WorkEmail = Flow.start(users.get(UID2))
                .then(User::getEmails)
                .then(mapping -> mapping.get(User.EmailField.WORK))
                .then(Email::getAddress)
                .getValue();
        String u2SchoolEmail = Flow.start(users.get(UID2))
                .then(User::getEmails)
                .then(mapping -> mapping.get(User.EmailField.SCHOOL))
                .then(Email::getAddress)
                .getValue();

        String u11PersonalEmail = Flow.start(users.get(UID1))
                .then(User::getEmails)
                .then(mapping -> mapping.get(User.EmailField.PERSONAL))
                .then(Email::getAddress)
                .getValue(() -> notFound);
        String u11WorkEmail = Flow.start(users.get(UID1))
                .then(User::getEmails)
                .then(mapping -> mapping.get(User.EmailField.WORK))
                .then(Email::getAddress)
                .getValue(() -> notFound);
        @SuppressWarnings("ConstantConditions") String u11SchoolEmail = Flow.start(users.get(UID1))
                .then(User::getEmails)
                .then(mapping -> mapping.get(User.EmailField.SCHOOL))
                .then(Email::getAddress)
                .getValue(() -> null);

        String uNPersonalEmail = Flow.start(users.get("not_included"))
                .then(User::getEmails)
                .then(mapping -> mapping.get(User.EmailField.PERSONAL))
                .then(Email::getAddress)
                .getValue();

        String uNNPersonalEmail = Flow.start(users.get("not_included"))
                .then(User::getEmails)
                .then(mapping -> mapping.get(User.EmailField.PERSONAL))
                .then(Email::getAddress)
                .getValue(() -> notFound);

        assertThat(u1PersonalEmail).isEqualTo(U_1_PERSONAL_EMAIL);
        assertThat(u1WorkEmail).isEqualTo(notFound);
        assertThat(u1SchoolEmail).isEqualTo(notFound);

        assertThat(u2PersonalEmail).isEqualTo(null);
        assertThat(u2WorkEmail).isEqualTo(U_2_WORK_EMAIL);
        assertThat(u2SchoolEmail).isEqualTo(U_2_SCHOOL_EMAIL);

        assertThat(u11PersonalEmail).isEqualTo(U_1_PERSONAL_EMAIL);
        assertThat(u11WorkEmail).isEqualTo(notFound);
        assertThat(u11SchoolEmail).isEqualTo(null);

        assertThat(uNPersonalEmail).isEqualTo(null);
        assertThat(uNNPersonalEmail).isEqualTo(notFound);
    }

    @NonNull
    private static Map<String, User> getUsers() {
        Map<User.EmailField, Email> u1Emails = new HashMap<>();
        u1Emails.put(User.EmailField.PERSONAL, new Email(U_1_PERSONAL_EMAIL));
        u1Emails.put(User.EmailField.SCHOOL, new Email(null));

        Map<User.EmailField, Email> u2Emails = new HashMap<>();
        u2Emails.put(User.EmailField.WORK, new Email(U_2_WORK_EMAIL));
        u2Emails.put(User.EmailField.SCHOOL, new Email(U_2_SCHOOL_EMAIL));

        Map<String, User> users = new HashMap<>();
        users.put(UID1, new User(u1Emails));
        users.put(UID2, new User(u2Emails));

        return users;
    }

    @Test
    public void flow_performance() {
        User[] user = getTestUser();
        long iterationAmount = 1000000;

        String flowEmail;
        long flowStart = System.nanoTime();
        for (long i = 0; i < iterationAmount; i++) {
            String email = Flow.start(user[0].getEmails())
                    .then(mapping -> mapping.get(User.EmailField.PERSONAL))
                    .then(Email::getAddress)
                    .getValue();

            mEmailStore[0] = email;
        }
        long flowDuration = System.nanoTime() - flowStart;
        flowEmail = mEmailStore[0];

        String shortFlowEmail;
        long shortFlowStart = System.nanoTime();
        for (long i = 0; i < iterationAmount; i++) {
            String email = flow(user[0].getEmails(),
                    map -> flow(map.get(User.EmailField.PERSONAL), Email::getAddress));

            mEmailStore[0] = email;
        }
        long shortFlowDuration = System.nanoTime() - shortFlowStart;
        shortFlowEmail = mEmailStore[0];

        String regularEmail;
        long regularStart = System.nanoTime();
        for (long i = 0; i < iterationAmount; i++) {
            Map<User.EmailField, Email> emails = user[0].getEmails();
            Email email;
            String emailAddress;
            if (emails != null) {
                email = emails.get(User.EmailField.PERSONAL);
                emailAddress = email != null ? email.getAddress() : null;
            } else {
                emailAddress = null;
            }

            mEmailStore[0] = emailAddress;
        }
        long regularDuration = System.nanoTime() - regularStart;
        regularEmail = mEmailStore[0];

        assertThat(flowEmail).isEqualTo(regularEmail);
        assertThat(shortFlowEmail).isEqualTo(regularEmail);

        printTestInfo("Flow", iterationAmount);

        printDuration("Flow", flowDuration, iterationAmount);
        printDuration("Short flow", shortFlowDuration, iterationAmount);
        printDuration( "Regular", regularDuration, iterationAmount);
        printDiff("flow - regular", flowDuration - regularDuration);
        printDiff("short flow - regular", shortFlowDuration - regularDuration);
    }

    @NonNull
    private User[] getTestUser() {
        Map<User.EmailField, Email> emails = new HashMap<>();
        emails.put(User.EmailField.PERSONAL, new Email("someone@example.com"));
        emails.put(User.EmailField.WORK, new Email(null));

        return new User[]{new User(emails)};
    }

    private static class Email {
        private final String mAddress;

        Email(@Nullable String address) {
            mAddress = address;
        }

        @Nullable
        String getAddress() {
            return mAddress;
        }
    }

    private static class User {
        private final Map<EmailField, Email> mEmails;

        User(@Nullable Map<EmailField, Email> emails) {
            mEmails = emails;
        }

        public Map<EmailField, Email> getEmails() {
            return mEmails;
        }

        private enum EmailField {
            PERSONAL, WORK, SCHOOL
        }
    }
}
