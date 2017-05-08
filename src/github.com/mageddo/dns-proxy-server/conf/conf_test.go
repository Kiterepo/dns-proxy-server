package conf

import (
	"testing"
	"fmt"
	"os"
	"github.com/mageddo/dns-proxy-server/utils/env"
	"io/ioutil"
	"github.com/stretchr/testify/assert"
)

func TestGetCurrentIpAddress(t *testing.T){

	ip, err := getCurrentIpAddress()
	if err != nil {
		t.Errorf(err.Error())
	}
	fmt.Println(">> " + ip)

}

func TestSetMachineDNSServer_EmptyFileSuccess(t *testing.T) {

	const TMP_RESOLV_FILE = "/tmp/test-resolv.conf"

	os.OpenFile(TMP_RESOLV_FILE, os.O_TRUNC, 0666)
	os.Setenv(env.MG_RESOLVCONF, TMP_RESOLV_FILE)
	err := SetMachineDNSServer("9.9.9.9")
	if err != nil {
		t.Error(err)
	}
	bytes, err := ioutil.ReadFile(TMP_RESOLV_FILE)
	if err != nil {
		t.Error(err)
	}
	assert.Equal(t, "nameserver 9.9.9.9 # dps-entry\n", string(bytes))

}

func TestSetMachineDNSServer_WithPreviousDnsServerAndCommentSuccess(t *testing.T) {

	const TMP_RESOLV_FILE = "/tmp/test-resolv.conf"
	os.Setenv(env.MG_RESOLVCONF, TMP_RESOLV_FILE)

	err := ioutil.WriteFile(TMP_RESOLV_FILE, []byte("# Provided by test\nnameserver 8.8.8.8"), 0666)
	if err != nil {
		t.Error(err)
	}

	err = SetMachineDNSServer("9.9.9.9")
	if err != nil {
		t.Error(err)
	}
	bytes, err := ioutil.ReadFile(TMP_RESOLV_FILE)
	if err != nil {
		t.Error(err)
	}
	fmt.Println(string(bytes))

	assert.Equal(t, `# Provided by test
# nameserver 8.8.8.8 # dps-comment
nameserver 9.9.9.9 # dps-entry
`, string(bytes))

}
func TestSetMachineDNSServer_WithPreviousDnsProxyServerAndCommentSuccess(t *testing.T) {

	const TMP_RESOLV_FILE = "/tmp/test-resolv.conf"
	os.Setenv(env.MG_RESOLVCONF, TMP_RESOLV_FILE)

	err := ioutil.WriteFile(TMP_RESOLV_FILE, []byte("# Provided by test\n# nameserver 7.7.7.7\nnameserver 8.8.8.8\n# nameserver 10.10.10 # dps-entry"), 0666)
	if err != nil {
		t.Error(err)
	}

	err = SetMachineDNSServer("9.9.9.9")
	if err != nil {
		t.Error(err)
	}
	bytes, err := ioutil.ReadFile(TMP_RESOLV_FILE)
	if err != nil {
		t.Error(err)
	}
	fmt.Println(string(bytes))

	assert.Equal(t, `# Provided by test
# nameserver 7.7.7.7
# nameserver 8.8.8.8 # dps-comment
nameserver 9.9.9.9 # dps-entry
`, string(bytes))

}


func TestRestoreResolvconfToDefault_Success(t *testing.T) {
	const TMP_RESOLV_FILE = "/tmp/test-resolv.conf"
	os.Setenv(env.MG_RESOLVCONF, TMP_RESOLV_FILE)

	err := ioutil.WriteFile(TMP_RESOLV_FILE, []byte("# Provided by test\n# nameserver 7.7.7.7\n# nameserver 8.8.8.8 # dps-comment\nnameserver 9.9.9.9 # dps-entry"), 0666)
	if err != nil {
		t.Error(err)
	}

	err = RestoreResolvconfToDefault()
	if err != nil {
		t.Error(err)
	}
	bytes, err := ioutil.ReadFile(TMP_RESOLV_FILE)
	if err != nil {
		t.Error(err)
	}
	fmt.Println(string(bytes))

	assert.Equal(t, `# Provided by test
# nameserver 7.7.7.7
nameserver 8.8.8.8
`, string(bytes))
}

func TestRestoreResolvconfToDefault_ConfFileAlreadyOk(t *testing.T) {
	const TMP_RESOLV_FILE = "/tmp/test-resolv.conf"
	os.Setenv(env.MG_RESOLVCONF, TMP_RESOLV_FILE)

	originalFileContent := "# Provided by test\n# nameserver 8.8.8.8\nnameserver 9.9.9.9\n"
	err := ioutil.WriteFile(TMP_RESOLV_FILE, []byte(originalFileContent), 0666)
	if err != nil {
		t.Error(err)
	}

	err = RestoreResolvconfToDefault()
	if err != nil {
		t.Error(err)
	}
	bytes, err := ioutil.ReadFile(TMP_RESOLV_FILE)
	if err != nil {
		t.Error(err)
	}
	fmt.Println(string(bytes))

	assert.Equal(t, originalFileContent, string(bytes))
}