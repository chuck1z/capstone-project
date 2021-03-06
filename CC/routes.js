const {
  root,
  getDetails,
  getFOTD,
  getTips,
  getTipsbyID,
  getLabel,
} = require("./handler");

const routes = [
  {
    method: "GET",
    path: "/",
    handler: root,
  },
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
  {
    method: "GET",
    path: "/fruits/tips",
    handler: getTips,
  },
  {
    method: "GET",
    path: "/fruits/tips/{id}",
    handler: getTipsbyID,
  },
  {
    method: "GET",
    path: "/models/label",
    handler: getLabel,
  },
];

module.exports = routes;
