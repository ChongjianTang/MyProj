var express = require("express");
var app = express();
var backendhost = process.env.BACKEND_HOST

const axios = require('axios');
const {response} = require("express");
app.use('/', express.static('static'))


const PORT = 80;
const HOST = "0.0.0.0";

app.listen(PORT, HOST,() => {
    console.log("Server running on port 80");
});

app.get("/greeting", (req, res, next) => {

    axios.get(backendhost + '/greeting')
        .then(response => {
            console.log(response.data);
            res.json(response.data.content);
        })
        .catch(error => {
            console.error(error);
        });
    // res.json({"Hello": "1"});
});
