<?php

/*
 * Following code will list all the products
 */

// array for JSON response
$response = array();

// include db connect class
require_once __DIR__ . '/include/DB_Connect.php';


// connecting to db

$db = new DB_CONNECT();


// get all products from products table
$result = mysqli_query($db->connect(),"SELECT *FROM questions") or die(mysqli_error());

// check for empty result
if (mysqli_num_rows($result) > 0) {
    // looping through all results
    // products node
    $response["questions"] = array();

    while ($row = mysqli_fetch_array($result)) {
        // temp user array
        $question = array();
        $question["id"] = $row["id"];
        $question["ques"] = $row["ques"];
        $question["a"] = $row["a"];
        $question["b"] = $row["b"];
        $question["c"] = $row["c"];
        $question["d"] = $row["d"];

        // push single product into final response array
        array_push($response["questions"], $question);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No questions found";

    // echo no users JSON
    echo json_encode($response);
}
?>