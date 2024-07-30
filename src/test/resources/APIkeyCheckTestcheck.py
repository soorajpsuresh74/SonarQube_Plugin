api_value = env.secret
key = "my_api_key" # Noncompliant {{Likely identified as sensitive key exposure. Please cross-verify. Found: 'my_api_key'}}
apiKeys = "1234567890abcdef" # Noncompliant {{Likely identified as sensitive key exposure. Please cross-verify. Found: '1234567890abcdef'}}