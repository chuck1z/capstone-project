const almanac = require("./almanac_small.json");
const articles = require("./articles.json");

function rand_from_seed(x, iterations) {
  iterations = iterations || 100;
  for (var i = 0; i < iterations; i++) x = (x ^ (x << 1) ^ (x >> 1)) % 10000;
  return x;
}

const root = () => {
  return {
    data: [
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
    ],
  };
};

const getFOTD = (h) => {
    // map needed response
    let data = almanac.map((x) => ({
      fruits_id: x.fruits_id,
      name: x.name,
      image: x.image,
    }));

  // date seed (daily -> 86400000)
  const len = data.length;
  var random = rand_from_seed(~~(new Date() / 86400000)) % len;

  // int to string
  random = random.toLocaleString("en-US", {
    minimumIntegerDigits: 4,
    useGrouping: false,
  });

  // add "f" in front
  random = "f".concat(random);



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
  // direct access to almanac.json
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

  let data = articles.map((x) => ({
    tips_id: x.tips_id,
    title: x.title,
    image: x.image,
    short_desc: x.short,
    date_posted: x.date_posted,
    full_desc: x.content,
  }));

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

  data = data.filter((n) => n.id === id)[0];

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

// const getModelLatest = (request, h) => {
//   if (data !== undefined) {
//     return {
//       error: false,
//       message: "Tips detail fetched successfully",
//       data,
//     };
//   }

//   const response = h.response({
//     status: "fail",
//     message: "Error not found",
//   });
//   response.code(404);
//   return response;
// };

const getLabel = (h) => {
  let temp = almanac.map((x) => ({
    id: x.fruits_id,
    name: x.name,
    about: x.about,
  }));

  for (let i = 0; i < temp.length; i++) {
    let ids = temp[i].id;
    ids = ids.slice(-2);

    id = parseInt(ids);

    let short_desc = temp[i].about;
    short_desc = short_desc.substr(0, 100);
    short_desc = short_desc.concat("...");

    // assign to data
    Object.assign(temp[i], { index: id });
    Object.assign(temp[i], { short_desc: short_desc });
  }

  const data = temp.map((x) => ({
    id: x.id,
    index: x.index,
    name: x.name,
    short_desc: x.short_desc,
  }));

  // console.log(Object.keys(data).length);
  // console.log(Object.values(data)[0]);
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
  root,
  getDetails,
  getFOTD,
  getTips,
  getTipsbyID,
  getLabel,
};
