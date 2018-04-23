<?php


// array for JSON response
$response = array();

// include db connect class
require_once __DIR__ . '/include/DB_Connect.php';

// connecting to db
$db = new DB_CONNECT();

// check for post data
if (isset($_GET["id"])) {
    $id = $_GET['id'];

    // get a product from products table
    $result = mysqli_query($db->connect(),"SELECT *FROM questions WHERE id = $id");

    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $result = mysqli_fetch_array($result);

            $question = array();
            $question["id"] = $result["id"];
            $question["ques"] = $result["ques"];
            $question["a"] = $result["a"];
            $question["b"] = $result["b"];
            $question["c"] = $result["c"];
            $question["d"] = $result["d"];
            // success
            $response["success"] = 1;

            // user node
            $response["question"] = array();

            array_push($response["question"], $question);

            // echoing JSON response
            echo json_encode($response);
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No question found";

            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No question found";

        // echo no users JSON
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