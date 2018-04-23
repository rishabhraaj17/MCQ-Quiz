<?php

/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['ques']) && isset($_POST['a']) && isset($_POST['b']) && isset($_POST['c']) && isset($_POST['d']) && isset($_POST['ans'])) {

    $ques = $_POST['ques'];
    $a = $_POST['a'];
    $b = $_POST['b'];
    $c = $_POST['c'];
    $d = $_POST['d'];
    $ans = $_POST['ans'];
    

    // include db connect class
    require_once __DIR__ . '/include/DB_Connect.php';

    // connecting to db
    $db = new DB_CONNECT();

    // mysql inserting a new row
    $result = mysqli_query($db->connect(),"INSERT INTO questions(ques, a, b, c, d, ans) VALUES('$ques', 'a', 'b', 'c', 'd', 'ans')");

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