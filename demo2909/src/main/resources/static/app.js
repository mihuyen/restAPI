const API_URL = 'http://localhost:8080/api';

// Check Auth & Redirect
function checkAuth() {
    const token = localStorage.getItem('token');
    if (!token && window.location.pathname.includes('dashboard.html')) {
        window.location.href = 'index.html';
    } else if (token && window.location.pathname.includes('index.html')) {
        window.location.href = 'dashboard.html';
    }
    if (token) loadBlogs();
}

// Login
const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        try {
            const res = await fetch(`${API_URL}/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });

            if (res.ok) {
                const data = await res.json();
                localStorage.setItem('token', data.token);
                localStorage.setItem('username', username); // Store for simple checks
                window.location.href = 'dashboard.html';
            } else {
                document.getElementById('message').innerText = 'Invalid credentials';
            }
        } catch (err) {
            document.getElementById('message').innerText = 'Network error';
        }
    });
}

// Logout
function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    window.location.href = 'index.html';
}

// Load Blogs
async function loadBlogs() {
    const token = localStorage.getItem('token');
    try {
        const res = await fetch(`${API_URL}/blogs`, {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        
        if (res.ok) {
            const blogs = await res.json();
            displayBlogs(blogs);
        } else if (res.status === 403) {
            logout(); // Token invalid
        }
    } catch (err) {
        console.error(err);
    }
}

// Display Blogs
function displayBlogs(blogs) {
    const container = document.getElementById('blogList');
    container.innerHTML = '';
    
    blogs.forEach(blog => {
        const currentUser = localStorage.getItem('username'); // Assumption: stored on login
        // Note: In real app, we decode JWT or fetch user profile to get role/id
        
        const div = document.createElement('div');
        div.className = 'blog-item';
        div.innerHTML = `
            <h3>${blog.title}</h3>
            <p>${blog.content}</p>
            <div class="blog-meta">Posted by User ID: ${blog.user ? blog.user.id : 'Unknown'}</div>
            <button onclick="deleteBlog(${blog.id})" class="btn-delete">Delete</button>
        `;
        container.appendChild(div);
    });
}

// Create Blog
const createBlogForm = document.getElementById('createBlogForm');
if (createBlogForm) {
    createBlogForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const title = document.getElementById('blogTitle').value;
        const content = document.getElementById('blogContent').value;
        const token = localStorage.getItem('token');

        const res = await fetch(`${API_URL}/blogs`, {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ title, content })
        });

        if (res.ok) {
            createBlogForm.reset();
            loadBlogs();
        } else {
            alert('Failed to post blog');
        }
    });
}

// Delete Blog
async function deleteBlog(id) {
    if (!confirm('Are you sure?')) return;
    const token = localStorage.getItem('token');
    
    const res = await fetch(`${API_URL}/blogs/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
    });

    if (res.ok) {
        loadBlogs();
    } else {
        alert('You are not authorized to delete this blog!');
    }
}
