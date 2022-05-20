const { nanoid } = require('nanoid')
const almanac = require("./almanac.json");

const addBookHandler = (request, h) => {
  const {
    name,
    year,
    author,
    summary,
    publisher,
    pageCount,
    readPage,
    reading
  } = request.payload

  if (name === undefined || name === '') {
    const response = h.response({
      status: 'fail',
      message: 'Gagal menambahkan buku. Mohon isi nama buku'
    })
    response.code(400)
    return response
  }

  if (readPage > pageCount) {
    const response = h.response({
      status: 'fail',
      message:
        'Gagal menambahkan buku. readPage tidak boleh lebih besar dari pageCount'
    })
    response.code(400)
    return response
  }

  const id = nanoid(16)
  const finished = pageCount === readPage
  const insertedAt = new Date().toISOString()
  const updatedAt = insertedAt

  const newBook = {
    id,
    name,
    year,
    author,
    summary,
    publisher,
    pageCount,
    readPage,
    finished,
    reading,
    insertedAt,
    updatedAt
  }

  books.push(newBook)

  const isSuccess = books.filter((book) => book.id === id).length > 0

  if (isSuccess) {
    const response = h.response({
      status: 'success',
      message: 'Buku berhasil ditambahkan',
      data: {
        bookId: id
      }
    })
    response.code(201)
    return response
  }

  const response = h.response({
    status: 'fail',
    message: 'Buku gagal ditambahkan'
  })

  response.code(500)
  return response
}

const getBooksHandler = (request, h) => {
  // When query is not empty
  if (Object.keys(request.query).length !== 0) {
    const { id, name } = request.query

    const temp = almanac.map((x) => ({
      id: x.id,
      name: x.name,
      publisher: x.publisher,
      reading: x.reading,
      finished: x.finished
    }))

    let book = temp

    if (name !== undefined) {
      console.log('name not empty')
      book = book.filter((n) =>
        n.name.toLowerCase().includes(name.toLowerCase())
      )
    }

    if (reading !== undefined) {
      console.log('reading not empty')
      let boolval = false
      if (reading === '1') {
        boolval = true
      }
      book = book.filter((n) => n.reading === boolval)
    }

    if (finished !== undefined) {
      console.log('finished not empty')
      let boolval = false
      if (finished === '1') {
        boolval = true
      }
      book = book.filter((n) => n.finished === boolval)
    }

    if (book !== undefined) {
      return {
        status: 'success',
        data: {
          books: book.map((x) => ({
            id: x.id,
            name: x.name,
            publisher: x.publisher
          }))
        }
      }
    }

    const response = h.response({
      status: 'fail',
      message: 'Buku tidak ditemukan'
    })
    response.code(404)
    return response
  }

  return {
    status: 'success',
    data: {
      books: books.map((x) => ({
        id: x.id,
        name: x.name,
        publisher: x.publisher
      }))
    }
  }
}

const getBookByIdHandler = (request, h) => {
  const { id } = request.params

  const book = books.filter((n) => n.id === id)[0]

  // Might need response code 200
  if (book !== undefined) {
    return {
      status: 'success',
      data: {
        book: book
      }
    }
  }

  const response = h.response({
    status: 'fail',
    message: 'Buku tidak ditemukan'
  })
  response.code(404)
  return response
}

const getBookByNameHandler = (request, h) => {
  const { query } = request.query

  const book = books.filter((n) => n.name.includes(query))

  // Might need response code 200
  if (book !== undefined) {
    return {
      status: 'success',
      data: {
        book: book
      }
    }
  }

  const response = h.response({
    status: 'fail',
    message: 'Buku tidak ditemukan'
  })
  response.code(404)
  return response
}

const editBookByIdHandler = (request, h) => {
  const { id } = request.params

  const {
    name,
    year,
    author,
    summary,
    publisher,
    pageCount,
    readPage,
    reading
  } = request.payload

  if (name === undefined || name === '' || readPage > pageCount) {
    // Watch out for this one
    let response
    if (name === undefined || name === '') {
      response = h.response({
        status: 'fail',
        message: 'Gagal memperbarui buku. Mohon isi nama buku'
      })
    }
    if (readPage > pageCount) {
      response = h.response({
        status: 'fail',
        message:
          'Gagal memperbarui buku. readPage tidak boleh lebih besar dari pageCount'
      })
    }
    response.code(400)
    return response
  }

  const insertedAt = new Date().toISOString()
  const updatedAt = insertedAt

  const finished = readPage === pageCount

  const index = books.findIndex((book) => book.id === id)

  if (index !== -1) {
    books[index] = {
      ...books[index],
      name,
      year,
      author,
      summary,
      publisher,
      pageCount,
      readPage,
      finished,
      reading,
      insertedAt,
      updatedAt
    }

    const response = h.response({
      status: 'success',
      message: 'Buku berhasil diperbarui'
    })
    response.code(200)
    return response
  }

  const response = h.response({
    status: 'fail',
    message: 'Gagal memperbarui buku. Id tidak ditemukan'
  })
  response.code(404)
  return response
}

const deleteBookByIdHandler = (request, h) => {
  const { id } = request.params

  const index = books.findIndex((book) => book.id === id)

  if (index !== -1) {
    books.splice(index, 1)
    const response = h.response({
      status: 'success',
      message: 'Buku berhasil dihapus'
    })
    response.code(200)
    return response
  }

  const response = h.response({
    status: 'fail',
    message: 'Buku gagal dihapus. Id tidak ditemukan'
  })
  response.code(404)
  return response
}

module.exports = {
  addBookHandler,
  getBooksHandler,
  getBookByIdHandler,
  getBookByNameHandler,
  editBookByIdHandler,
  deleteBookByIdHandler
}
