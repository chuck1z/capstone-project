const { getDetails } = require("./handlertest");

const routes = [
  {
    method: "GET",
    path: "/details/{name}",
    handler: getDetails,
  },
];

module.exports = routes;
