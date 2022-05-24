const { getDetails } = require("./handler");

const routes = [
  {
    method: "GET",
    path: "/details/{name}",
    handler: getDetails,
  },
];

module.exports = routes;
