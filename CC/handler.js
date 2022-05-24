const almanac = require("./almanac.json");

const getDetails = (request, h) => {
  const { name } = request.params;

  const data = almanac.filter((n) => n.name === name)[0];

  // Might need response code 200
  if (data !== undefined) {
    return {
      status: "success",
      data: {
        almanac: data,
      },
    };
  }

  const response = h.response({
    status: "fail",
    message: "Error not found",
  });
  response.code(404);
  return response;
};

module.exports = {
  getDetails,
};
