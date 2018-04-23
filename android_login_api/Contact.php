<?php

/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['ID']) && isset($_POST['U_ID']) && isset($_POST['U_Name']) && isset($_POST['U_Email']) && isset($_POST['MSG'])) {

    $id = $_POST['ID'];
    $u_id = $_POST['U_ID'];
    $u_name = $_POST['U_Name'];
    $u_email = $_POST['U_Email'];
    $msg = $_POST['MSG'];



    // include db connect class
    require_once __DIR__ . '/include/DB_Connect.php';

    // connecting to db
    $db = new DB_CONNECT();

    // mysql inserting a new row
    $result = mysqli_query($db->connect(),"INSERT INTO contact(ID, U_ID, U_NAME, U_EMAIL, MSG) VALUES('$id', '$u_id', '$u_name', '$u_email', '$msg')");

    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Question created.";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";

        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>