package com.connect.connectcom.uberactivity;

import java.text.SimpleDateFormat;

/**
 * Created by simon on 04/12/14.
 */
public class Constants {
    public static final String HEADER_PLATFORM = "X-CN-PLATFORM";
    public static final String HEADER_VERSION = "X-CN-VERSION";
    public static final String HEADER_APP_VERSION = "X-CN-APP-VERSION";

    // Date formats
    public static final String DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING);

    // Location
    public static final String GET_DIRECTIONS_URI = "http://maps.google.com/maps?f=d&hl=en&daddr=%f,%f";
    public static final String SHARE_LOCATION_DLG_FRAGMENT = "share_location_dlg_fragment";

    public static final String SHARE_LOCATION_URL = "http://maps.google.com/?ie=UTF8&q=%f,%f&ll=%f,%f&z=13";
    public static final String SHARE_LOCATION_WITH_NAME_URL = "http://maps.google.com/?ie=UTF8&q=%s&ll=%f,%f&z=13";

    // SMS
    public static final String SMS_BODY_EXTRA = "sms_body";
    public static final String MMS_SMS_TYPE = "vnd.android-dir/mms-sms";
    public static final String TEXT_PLAIN_TYPE = "text/plain";
    public static final String TEXT_HTML_TYPE = "text/html";

    // Hash values
    public static final int HASH_EQUAL = 10;
    public static final int HASH_CHANGED = 20;
    public static final int HASH_NOT_FOUND = 30;

    // Broadcast Intents
    public static final String ACTION_RTL_STATE_CHANGED =
            "com.connect" + ".ACTION_RTL_STATE_CHANGED";
    public static final String ACTION_FIRST_ACTIVITY_ARRIVED =
            "com.connect" + ".ACTION_FIRST_ACTIVITY_ARRIVED";
    public static final String CONNECTIONS_UPDATED_BROADCAST =
            "com.connect" + ".CONNECTIONS_UPDATED_BROADCAST";
    public static final String IMPORT_CONNECTIONS_FIRST_PAGE_COMPLETE_BROADCAST =
            "com.connect" + ".IMPORT_CONNECTIONS_FIRST_PAGE_COMPLETE_BROADCAST";
    public static final String IMPORT_CONNECTIONS_COMPLETE_BROADCAST =
            "com.connect" + ".IMPORT_CONNECTIONS_COMPLETE_BROADCAST";
    public static final String NO_CONNECTIVITY_BROADCAST =
            "com.connect" + ".NO_CONNECTIVITY_BROADCAST";
    public static final String CONNECTIVITY_RESTORED_BROADCAST =
            "com.connect" + ".CONNECTIVITY_RESTORED_BROADCAST";
    public static final String BADGE_COUNT_UPDATED_BROADCAST =
            "com.connect" + ".BADGE_COUNT_UPDATED_BROADCAST";
    public static final String SOURCE_SYNC_FINISHED_BROADCAST =
            "com.connect" + ".SOURCE_SYNC_FINISHED_BROADCAST";
    public static final String SOURCE_SYNC_ERROR_BROADCAST =
            "com.connect" + ".SOURCE_SYNC_ERROR_BROADCAST";
    public static final String INITIAL_IMPORT_STARTED_BROADCAST =
            "com.connect" + ".INITIAL_IMPORT_STARTED_BROADCAST";
    public static final String NO_CONNECTIONS_BROADCAST =
            "com.connect" + ".NO_CONNECTIONS_BROADCAST";
    public static final String ACTION_CONNECTION_UPDATED =
            "com.connect.connectcom" + ".ACTION_CONNECTION_UPDATED";
    public static final String HOME_CITY_UPDATED =
            "com.connect.connectcom" + ".HOME_CITY_UPDATED";

    // Pusher Events
    public static final String EVENT_MESSAGE_CREATED = "message.created";
    public static final String EVENT_MEMBER_ADDED = "member.added";
    public static final String EVENT_MEMBER_REMOVED = "member.removed";
    public static final String EVENT_REAL_TIME_STARTED = "realtime.started";
    public static final String EVENT_REAL_TIME_STOPPED = "realtime.stopped";
    public static final String EVENT_REAL_TIME_EXPIRED = "realtime.expired";
    public static final String EVENT_INVITE_ACCEPTED = "invite.accepted";
    public static final String EVENT_INVITE_CREATED = "invite.created";
    public static final String EVENT_INVITE_REMOVED = "invite.removed";
    public static final String EVENT_CONNECTION_UPDATED = "connection.updated";
    public static final String EVENT_NOTIFICATION_CREATED = "notification.created";

    // Push Notification Types
    public static final String PUSH_TYPE_MESSAGE_CREATED = "message.created";
    public static final String PUSH_TYPE_CHECKIN = "connection.checkin";
    public static final String PUSH_TYPE_MEMBER_ADDED = "member.added";
    public static final String PUSH_TYPE_POST_CREATED = "post.created";
    public static final String PUSH_TYPE_EVENT_NEARBY = "event.nearby";
    public static final String PUSH_TYPE_EVENT_CHECKIN = "event.checkin";
    public static final String PUSH_TYPE_SOCIAL_PROOF = "socialproof";

    public static final String EVENTS_UPDATED_BROADCAST = "com.connect.EVENTS_UPDATED_BROADCAST";
    public static final String PUSH_TYPE_NULL = "com.connect.PUSH_TYPE_NULL";

    // Campaign Tracking/Invite Redeeming
    public static final String DEEP_LINK_HOST_ONE = "s.connect.com";
    public static final String DEEP_LINK_HOST_TWO = "share.connect.com";
    public static final String DEEP_LINK_HOST_THREE = "s-staging.connect.com";
    public static final String DEEP_LINK_HOST_FOUR = "share-staging.connect.com";
    public static final String TYPE_CHAT_INVITE = "c=chatinvite";
    public static final String TYPE_GROUP_LINK = "c=groupLink";
    public static final String KEY_ACTION = "r";
    public static final String ACTION_REDEEM_INVITE = "redeeminvite";
    public static final String KEY_ID = "i";
    public static final String REFERRER_LINK = "referrer";

    public static final String UTM_SOURCE_CONNECT = "utm_source=connect";
    public static final String UTM_SOURCE_OTHER = "utm_source=other_connect";
    public static final String REDEEM_INVITE = "utm_content=redeeminvite/";

    public static final String UTM_SOURCE = "utm_source";
    public static final String UTM_CONTENT = "utm_content";

    public static final String SOURCE_CONNECT = "connect";
    public static final String SOURCE_OTHER = "other_connect";
    public static final String CONTENT_REDEEM_INVITE = "redeeminvite/";
    public static final String EMPTY_STRING = "";

    //events
    public static final float METERS_CONVERSION = 1609.344f;
    public static final float LIMIT_RADIUS = 0.03f; //30 miles

    // Log entries with this tag will go to the in-app logging tool
    public static final String QA_FILTER_TAG = "QA_ENTRY";

    // Amazon S3
    public static final String AMAZON_AWS_ACCOUNT_ID = "242641382134";
    public static final String COGNITO_IDENTITY_POOL = "us-east-1:36b449d5-58d4-49b2-a238-4d27191f1e85";
    public static final String AMAZON_S3_BUCKET = "connect.uploads";
    public static final String AMAZON_AUTH_ROLE_ARN =
            "arn:aws:iam::242641382134:role/Cognito_ConnectiOSS3Auth_DefaultRole";
    public static final String AMAZON_UNAUTH_ROLE_ARN =
            "arn:aws:iam::242641382134:role/Cognito_ConnectiOSS3Unauth_DefaultRole";

    // Uber
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    // Iterable
    public static final String API_KEY_QUERY = "api_key";
    public static final String ITERABLE_API_KEY = "ed41204110ee40c780a076df68b23f17";
    public static final String ITERABLE_ENDPOINT = "http://api.iterable.com/api";

    // Google Plus
    public static final String READ_CONTACTS_SCOPE = "https://www.google.com/m8/feeds/";
    public static final String SERVER_CLIENT_ID = "251253567538.apps.googleusercontent.com";
    public static final String SERVER_AUTH_SCOPE = String.format(
            "oauth2:server:client_id:%s:api_scope:%s",
            SERVER_CLIENT_ID,
            READ_CONTACTS_SCOPE
    );

    // Misc.
    public static final String PLATFORM_ANDROID = "android";
    public static final String EXTRA_PHONE_NUMBER = "com.connect.connectcom.Constants.EXTRA_PHONE_NUMBER";
    public static final String EXTRA_AREA_CODE = "com.connect.connectcom.Constants.EXTRA_AREA_CODE";
    public static final String EXTRA_SMS_CODE = "com.connect.connnectcom.Constants.EXTRA_SMS_CODE";
    public static final String ARG_ADAPTER_TYPE = "com.connect.ARG_ADAPTER_TYPE";

    public static final String ACTION_INVITE_STATUS_CHANGED = "com.connect.connectcom.ACTION_INVITE_STATUS_CHANGED";
    public static final String EXTRA_CONNECTION_ID = "com.connect.connectcom.EXTRA_CONNECTION_ID";
    public static final String EXTRA_INVITE_STATUS_ACTIVE = "com.connect.connectcom.EXTRA_INVITE_STATUS_ACTIVE";

    public static final String ACTION_INVITE_STATUS_REQUESTED = "com.connect.connectcom.ACTION_INVITE_STATUS_REQUESTED";
    public static final String EXTRA_REQUEST_UPDATED_INFO = "com.connect.connectcom.EXTRA_REQUEST_UPDATED_INFO";

    public static final String KEY_STATE_HIDDEN = "com.connect.connectcom.KEY_STATE_HIDDEN";
    public static final String KEY_LAST_SHOWN_FRAGMENT = "com.connect.KEY_LAST_SHOWN_FRAGMENT";

    public static final String CONNECT_ACTION = "com.connect";
    public static final String CONNECT_DEEP_LINK_SCHEME = "connectapp://";

    // SyncAdapter framework
    public static final String AUTHORITY = "com.connect.connectcom.provider";
    public static final String ACCOUNT_TYPE = "connect.com";
    public static final String ACCOUNT = "connectAccount";
    public static final String KEY_GROUP_ID = "com.connect.connectcom.KEY_GROUP_ID";
}
