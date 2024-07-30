# Test file for SonarQube rule detecting hardcoded JWTs

# Example of hardcoded JWTs
jwt_token_hardcoded = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"  # Noncompliant {{Avoid hardcoded JWT tokens for security reasons. Found: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c'}}

def retrieve_token():
    # This is an example of a hardcoded JWT within a function
    token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkphbmUgRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.YnJhbmNoQkRBUzEyMw=="  # Noncompliant {{Avoid hardcoded JWT tokens for security reasons. Found: 'eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkphbmUgRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.YnJhbmNoQkRBUzEyMw=='}}
    return token

# Edge case: A string that looks similar to a JWT but is not base64 encoded correctly
fake_jwt_token = "invalid.jwt.token"  # Compliant - Not a real JWT

# Another example where a JWT is in a list
tokens_list = [
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
    "another_fake.jwt.token"  # Compliant - Not a real JWT
]

def process_tokens():
    for token in tokens_list:
        if token == fake_jwt_token:
            print("This is not a real JWT")


# deal in future release
# Additional test case: JWT in a dictionary
def get_tokens():
    return {
        "auth_token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkphbmUgRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.YnJhbmNoQkRBUzEyMw==",
        "refresh_token": fake_jwt_token  # Compliant
    }
