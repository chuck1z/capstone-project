const { getDetails, getFOTD } = require("./handler");

const routes = [
  {
    method: "GET",
    path: "/fruits/{id}/detail",
    handler: getDetails,
  },
  {
    method: "GET",
    path: "/fruits/fotd",
    handler: getFOTD,
  },
];

module.exports = routes;
