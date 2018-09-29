# Sets the minimum version of CMake required to build your native library.
# This ensures that a certain set of CMake features is available to
# your build.

cmake_minimum_required(VERSION 3.4.1)

# Specifies a library name, specifies whether the library is STATIC or
# SHARED, and provides relative paths to the source code. You can
# define multiple libraries by adding multiple add_library() commands,
# and CMake builds them for you. When you build your app, Gradle
# automatically packages shared libraries with your APK.

# now build app's shared lib
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c99 -DANDROID_STL=gnustl_static -DANDROID_TOOLCHAIN=clang")

# -Wimplicit-function-declaration
# -Wno-missing-prototypes -Werror=return-type -Wdocumentation -Wunreachable-code-aggressive -Wno-missing-braces
# -Wparentheses -Wswitch -Wno-unused-function -Wunused-label -Wno-unused-parameter -Wunused-variable -Wunused-value -Wempty-body
# -Wconditional-uninitialized -Wno-unknown-pragmas -pedantic -Wshadow -Wfour-char-constants -Wno-conversion -Wconstant-conversion
# -Wint-conversion -Wbool-conversion -Wenum-conversion -Wassign-enum -Wno-shorten-64-to-32 -Wpointer-sign -Wnewline-eof
# -Wdeprecated-declarations -Wno-sign-conversion

add_library( # The name of the library.
             core

             # Shared library.
             SHARED

             # Core files
             src/main/jni/sumpay-core/BRAddress.c
             src/main/jni/sumpay-core/BRAddress.h
             src/main/jni/sumpay-core/BRArray.h
             src/main/jni/sumpay-core/BRBase58.c
             src/main/jni/sumpay-core/BRBase58.h
             src/main/jni/sumpay-core/BRBech32.c
             src/main/jni/sumpay-core/BRBech32.h
             src/main/jni/sumpay-core/BRBIP32Sequence.c
             src/main/jni/sumpay-core/BRBIP32Sequence.h
             src/main/jni/sumpay-core/BRBIP38Key.c
             src/main/jni/sumpay-core/BRBIP38Key.h
             src/main/jni/sumpay-core/BRBIP39Mnemonic.c
             src/main/jni/sumpay-core/BRBIP39Mnemonic.h
             src/main/jni/sumpay-core/BRBIP39WordsEn.h
             src/main/jni/sumpay-core/BRBloomFilter.c
             src/main/jni/sumpay-core/BRBloomFilter.h
             src/main/jni/sumpay-core/BRCrypto.c
             src/main/jni/sumpay-core/BRCrypto.h
             src/main/jni/sumpay-core/BRInt.h
             src/main/jni/sumpay-core/BRKey.c
             src/main/jni/sumpay-core/BRKey.h
             src/main/jni/sumpay-core/BRKeyECIES.h
             src/main/jni/sumpay-core/BRKEYECIES.c
             src/main/jni/sumpay-core/BRMerkleBlock.c
             src/main/jni/sumpay-core/BRMerkleBlock.h
             src/main/jni/sumpay-core/BRPaymentProtocol.c
             src/main/jni/sumpay-core/BRPaymentProtocol.h
             src/main/jni/sumpay-core/BRPeer.c
             src/main/jni/sumpay-core/BRPeer.h
             src/main/jni/sumpay-core/BRPeerManager.c
             src/main/jni/sumpay-core/BRPeerManager.h
             src/main/jni/sumpay-core/BRSet.c
             src/main/jni/sumpay-core/BRSet.h
             src/main/jni/sumpay-core/BRTransaction.c
             src/main/jni/sumpay-core/BRTransaction.h
             src/main/jni/sumpay-core/BRWallet.c
             src/main/jni/sumpay-core/BRWallet.h

             # BCash files
             src/main/jni/sumpay-core/bcash/BRBCashAddr.c
             src/main/jni/sumpay-core/bcash/BRBCashAddr.h
             src/main/jni/sumpay-core/bcash/BRBCashParams.h

             # Core JNI files
             src/main/jni/sumpay-core/Java/BRCoreJni.h
             src/main/jni/sumpay-core/Java/BRCoreJni.c
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreJniReference.c
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreJniReference.h
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreAddress.c
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreAddress.h
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreChainParams.c
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreChainParams.h
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCorePeer.c
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCorePeer.h
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreMasterPubKey.c
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreMasterPubKey.h
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreKey.h
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreKey.c
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreMerkleBlock.c
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreMerkleBlock.h
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCorePeerManager.c
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCorePeerManager.h
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreTransaction.c
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreTransaction.h
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreTransactionInput.c
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreTransactionInput.h
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreTransactionOutput.c
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreTransactionOutput.h
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCorePaymentProtocol.c
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCorePaymentProtocol.h
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreWallet.c
             src/main/jni/sumpay-core/Java/com_breadwallet_core_BRCoreWallet.h

             # Bech32 files
             src/main/jni/sumpay-core/BRBech32.c
             src/main/jni/sumpay-core/BRBech32.h

             # Ethereum files
             src/main/jni/sumpay-core/ethereum/BREthereum.h
             src/main/jni/sumpay-core/ethereum/BREthereum.c
             src/main/jni/sumpay-core/ethereum/BREthereumAccount.c
             src/main/jni/sumpay-core/ethereum/BREthereumAccount.h
             src/main/jni/sumpay-core/ethereum/BREthereumAccountState.c
             src/main/jni/sumpay-core/ethereum/BREthereumAccountState.h
             src/main/jni/sumpay-core/ethereum/BREthereumAmount.c
             src/main/jni/sumpay-core/ethereum/BREthereumAmount.h
             src/main/jni/sumpay-core/ethereum/BREthereumBase.h
             src/main/jni/sumpay-core/ethereum/BREthereumBlock.c
             src/main/jni/sumpay-core/ethereum/BREthereumBlock.h
             src/main/jni/sumpay-core/ethereum/BREthereumBloomFilter.c
             src/main/jni/sumpay-core/ethereum/BREthereumBloomFilter.h
             src/main/jni/sumpay-core/ethereum/BREthereumContract.c
             src/main/jni/sumpay-core/ethereum/BREthereumContract.h
             src/main/jni/sumpay-core/ethereum/BREthereumEther.c
             src/main/jni/sumpay-core/ethereum/BREthereumEther.h
             src/main/jni/sumpay-core/ethereum/BREthereumGas.c
             src/main/jni/sumpay-core/ethereum/BREthereumGas.h
             src/main/jni/sumpay-core/ethereum/BREthereumHash.c
             src/main/jni/sumpay-core/ethereum/BREthereumLightNode.c
             src/main/jni/sumpay-core/ethereum/BREthereumLightNode.h
             src/main/jni/sumpay-core/ethereum/BREthereumLightNodeAnnounce.c
             src/main/jni/sumpay-core/ethereum/BREthereumLightNodeListen.c
             src/main/jni/sumpay-core/ethereum/BREthereumLightNodePrivate.h
             src/main/jni/sumpay-core/ethereum/BREthereumLog.c
             src/main/jni/sumpay-core/ethereum/BREthereumLog.h
             src/main/jni/sumpay-core/ethereum/BREthereumNetwork.c
             src/main/jni/sumpay-core/ethereum/BREthereumNetwork.h
             src/main/jni/sumpay-core/ethereum/BREthereumPrivate.h
             src/main/jni/sumpay-core/ethereum/BREthereumToken.c
             src/main/jni/sumpay-core/ethereum/BREthereumToken.h
             src/main/jni/sumpay-core/ethereum/BREthereumTransaction.c
             src/main/jni/sumpay-core/ethereum/BREthereumTransaction.h
             src/main/jni/sumpay-core/ethereum/BREthereumTransactionReceipt.c
             src/main/jni/sumpay-core/ethereum/BREthereumTransactionReceipt.h
             src/main/jni/sumpay-core/ethereum/BREthereumWallet.c
             src/main/jni/sumpay-core/ethereum/BREthereumWallet.h

             # Ethereum Event
             src/main/jni/sumpay-core/ethereum/event/BREvent.c
             src/main/jni/sumpay-core/ethereum/event/BREvent.h
             src/main/jni/sumpay-core/ethereum/event/BREventQueue.c
             src/main/jni/sumpay-core/ethereum/event/BREventQueue.h

             #Ethereum RLP
             src/main/jni/sumpay-core/ethereum/rlp/BRRlp.h
             src/main/jni/sumpay-core/ethereum/rlp/BRRlpCoder.c
             src/main/jni/sumpay-core/ethereum/rlp/BRRlpCoder.h

             #Ethereum Util
             src/main/jni/sumpay-core/ethereum/util/BRUtil.h
             src/main/jni/sumpay-core/ethereum/util/BRUtilHex.c
             src/main/jni/sumpay-core/ethereum/util/BRUtilHex.h
             src/main/jni/sumpay-core/ethereum/util/BRUtilMath.c
             src/main/jni/sumpay-core/ethereum/util/BRUtilMath.h
             src/main/jni/sumpay-core/ethereum/util/BRUtilMathParse.c

             #Ethereum JNI files
             src/main/jni/sumpay-core/ethereum/Java/com_breadwallet_core_ethereum_BREthereumAccount.h
             src/main/jni/sumpay-core/ethereum/Java/com_breadwallet_core_ethereum_BREthereumAmount.h
             src/main/jni/sumpay-core/ethereum/Java/com_breadwallet_core_ethereum_BREthereumLightNode.c
             src/main/jni/sumpay-core/ethereum/Java/com_breadwallet_core_ethereum_BREthereumLightNode.h
             src/main/jni/sumpay-core/ethereum/Java/com_breadwallet_core_ethereum_BREthereumNetwork.c
             src/main/jni/sumpay-core/ethereum/Java/com_breadwallet_core_ethereum_BREthereumNetwork.h
             src/main/jni/sumpay-core/ethereum/Java/com_breadwallet_core_ethereum_BREthereumToken.c
             src/main/jni/sumpay-core/ethereum/Java/com_breadwallet_core_ethereum_BREthereumToken.h
             src/main/jni/sumpay-core/ethereum/Java/com_breadwallet_core_ethereum_BREthereumTransaction.h
             src/main/jni/sumpay-core/ethereum/Java/com_breadwallet_core_ethereum_BREthereumWallet.h

             )

include_directories(src/main/jni/sumpay-core/ethereum/rlp/)
include_directories(src/main/jni/sumpay-core/ethereum/util/)
include_directories(src/main/jni/sumpay-core/ethereum/Java/)
include_directories(src/main/jni/sumpay-core/ethereum/)
include_directories(src/main/jni/sumpay-core/bcash/)
include_directories(src/main/jni/sumpay-core/Java/)
include_directories(src/main/jni/sumpay-core/)
include_directories(src/main/jni/sumpay-core/secp256k1/include/)
include_directories(src/main/jni/sumpay-core/secp256k1/src/)
include_directories(src/main/jni/sumpay-core/secp256k1/)

include_directories(src/main/secp/secp256k1/include/)
include_directories(src/main/secp/secp256k1/src/)
include_directories(src/main/secp/secp256k1/)
include_directories(src/main/secp/)

# add lib dependencies
target_link_libraries(
    core
    android
    log)
