const almanac = require("./almanac.json");
const articles = require("./articles.json");

function rand_from_seed(x, iterations) {
  iterations = iterations || 100;
  for (var i = 0; i < iterations; i++) x = (x ^ (x << 1) ^ (x >> 1)) % 10000;
  return x;
}

const getFOTD = (h) => {
  // date seed (daily -> 86400000 and %22 -> 22 fruits)
  var random = rand_from_seed(~~(new Date() / 86400000)) % 22;

  // int to string
  random = random.toLocaleString("en-US", {
    minimumIntegerDigits: 4,
    useGrouping: false,
  });

  // add "f" in front
  random = "f".concat(random);

  // map needed response
  let data = almanac.map((x) => ({
    fruits_id: x.fruits_id,
    name: x.name,
    image: x.image,
  }));

  // filter by id
  data = data.filter((n) => n.fruits_id === random)[0];

  // "about" preview here (100 words) for home page
  temp = almanac.filter((n) => n.fruits_id === random)[0];
  let short_desc = temp.about;
  short_desc = short_desc.substr(0, 100);
  short_desc = short_desc.concat("...");

  // assign to data
  Object.assign(data, { short_desc: short_desc });

  // Might need response code 200
  if (data !== undefined) {
    return {
      error: false,
      message: "Fruits of The Day fetched successfully",
      data,
    };
  }

  const response = h.response({
    error: true,
    message: "Error not found",
  });
  response.code(404);
  return response;
};

const getTips = (h) => {
  const data = articles.map((x) => ({
    tips_id: x.tips_id,
    title: x.title,
    image: x.image,
    short_desc: x.short,
  }));

  if (data !== undefined) {
    return {
      error: false,
      message: "Tips fetched successfully",
      data,
    };
  }

  const response = h.response({
    status: "fail",
    message: "Error not found",
  });
  response.code(404);
  return response;
};

const getTipsbyID = (request, h) => {
  const { id } = request.params;
  console.log(id);

  let data = articles.map((x) => ({
    tips_id: x.tips_id,
    title: x.title,
    image: x.image,
    short_desc: x.short,
    date_posted: x.date_posted,
    full_desc: x.content,
  }));

  console.log(data.filter((n) => n.tips_id === id)[0]);
  data = data.filter((n) => n.tips_id === id)[0];

  if (data !== undefined) {
    return {
      error: false,
      message: "Tips detail fetched successfully",
      data,
    };
  }

  const response = h.response({
    status: "fail",
    message: "Error not found",
  });
  response.code(404);
  return response;
};

const getDetails = (request, h) => {
  const { id } = request.params;

  let data = almanac.map((x) => ({
    id: x.fruits_id,
    name: x.name,
    binom: x.binom,
    nutrition: x.nutrition,
    image: x.image,
    vitamin: x.vitamin,
    about: x.about,
    tips: x.tips,
  }));

  data = data.filter((n) => n.fruits_id === id)[0];

  // Might need response code 200
  if (data !== undefined) {
    return {
      error: false,
      message: "Tips detail fetched successfully",
      data,
    };
  }

  const response = h.response({
    status: "fail",
    message: "Error not found",
  });
  response.code(404);
  return response;
};

const getModelLatest = (request, h) => {
  if (data !== undefined) {
    return {
      error: false,
      message: "Tips detail fetched successfully",
      data,
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
  getFOTD,
  getTips,
  getTipsbyID,
};
